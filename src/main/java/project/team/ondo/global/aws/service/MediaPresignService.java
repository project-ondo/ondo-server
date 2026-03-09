package project.team.ondo.global.aws.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.aws.data.request.BatchPresignDownloadRequest;
import project.team.ondo.global.aws.data.request.PresignUploadRequest;
import project.team.ondo.global.aws.data.response.BatchPresignDownloadResponse;
import project.team.ondo.global.aws.data.response.PresignDownloadResponse;
import project.team.ondo.global.aws.data.response.PresignUploadResponse;
import project.team.ondo.global.aws.exception.UnsupportedMediaTypeException;
import project.team.ondo.global.data.S3Environment;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MediaPresignService {

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    private final S3Presigner s3Presigner;
    private final S3Environment s3Environment;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional(readOnly = true)
    public PresignUploadResponse presignUpload(PresignUploadRequest request) {
        UserEntity me = currentUserProvider.getCurrentUser();

        String contentType = request.contentType();
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) throw new UnsupportedMediaTypeException();

        String ext = extensionOf(contentType);
        String key = switch (request.category()) {
            case PROFILE -> buildProfileKey(me.getPublicId(), ext);
            case CHAT -> buildChatKeyWithPermission(me, request.chatRoomPublicId(), ext);
        };

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Environment.bucket())
                .key(key)
                .contentType(contentType)
                .build();

        long putTtl = s3Environment.presign().putExpirationSeconds();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(putTtl))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);

        LocalDateTime expiresAt = LocalDateTime.now(KST).plusSeconds(putTtl);

        return new PresignUploadResponse(key, presigned.url().toString(), expiresAt);
    }

    @Transactional(readOnly = true)
    public PresignDownloadResponse presignDownload(String key) {
        UserEntity me = currentUserProvider.getCurrentUser();

        validateKeyReadPermission(me, key);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Environment.bucket())
                .key(key)
                .build();

        long getTtl = s3Environment.presign().getExpirationSeconds();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(getTtl))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);

        LocalDateTime expiresAt = LocalDateTime.now(KST).plusSeconds(getTtl);

        return new PresignDownloadResponse(presigned.url().toString(), expiresAt);
    }

    @Transactional(readOnly = true)
    public BatchPresignDownloadResponse presignDownloadBatch(BatchPresignDownloadRequest request) {
        UserEntity me = currentUserProvider.getCurrentUser();

        Set<String> normalizedKeys = new LinkedHashSet<>();
        for (String raw : request.keys()) {
            if (raw == null) continue;
            String key = raw.trim();
            if (key.isBlank()) continue;
            normalizedKeys.add(key);
        }

        if (normalizedKeys.isEmpty()) {
            throw new IllegalArgumentException("KEY_REQUIRED");
        }

        long getTtl = s3Environment.presign().getExpirationSeconds();
        LocalDateTime expiresAt = LocalDateTime.now(KST).plusSeconds(getTtl);

        List<BatchPresignDownloadResponse.BatchPresignDownloadItem> items = new ArrayList<>(normalizedKeys.size());

        for (String key : normalizedKeys) {
            validateKeyReadPermission(me, key);

            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Environment.bucket())
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofSeconds(getTtl))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presigned = s3Presigner.presignGetObject(presignRequest);

            items.add(new BatchPresignDownloadResponse.BatchPresignDownloadItem(
                    key,
                    presigned.url().toString(),
                    expiresAt
            ));
        }

        return new BatchPresignDownloadResponse(List.copyOf(items));
    }

    private String buildProfileKey(UUID userPublicId, String ext) {
        return "profile/" + userPublicId + "/" + UUID.randomUUID() + "." + ext;
    }

    private String buildChatKeyWithPermission(UserEntity me, UUID roomPublicId, String ext) {
        if (roomPublicId == null) throw new IllegalArgumentException("ROOM_ID_REQUIRED");

        ChatRoomEntity room = chatRoomRepository.findByPublicId(roomPublicId)
                .orElseThrow(ChatRoomNotFoundException::new);

        chatRoomMemberRepository.findByRoomIdAndUserId(room.getId(), me.getId())
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        String ymd = LocalDate.now(KST).format(DateTimeFormatter.BASIC_ISO_DATE);
        return "chat/" + roomPublicId + "/" + ymd + "/" + UUID.randomUUID() + "." + ext;
    }

    private void validateKeyReadPermission(UserEntity me, String key) {
        if (key == null || key.isBlank()) throw new IllegalArgumentException("KEY_REQUIRED");

        if (key.startsWith("profile/")) {
            String[] parts = key.split("/");
            if (parts.length < 3) throw new IllegalArgumentException("INVALID_KEY");

            UUID userPublicId;
            try {
                userPublicId = UUID.fromString(parts[1]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("INVALID_KEY");
            }

            if (!userRepository.existsByPublicId(userPublicId)) {
                throw new IllegalArgumentException("USER_NOT_FOUND");
            }

            return;
        }

        if (key.startsWith("chat/")) {
            String[] parts = key.split("/");
            if (parts.length < 4) throw new IllegalArgumentException("INVALID_KEY");

            UUID roomPublicId;
            try {
                roomPublicId = UUID.fromString(parts[1]);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("INVALID_KEY");
            }

            ChatRoomEntity room = chatRoomRepository.findByPublicId(roomPublicId)
                    .orElseThrow(ChatRoomNotFoundException::new);

            chatRoomMemberRepository.findByRoomIdAndUserId(room.getId(), me.getId())
                    .orElseThrow(ChatRoomMemberNotFoundException::new);

            return;
        }

        throw new IllegalArgumentException("INVALID_KEY_PREFIX");
    }

    private String extensionOf(String contentType) {
        return switch (contentType) {
            case "image/jpeg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> throw new IllegalArgumentException("UNSUPPORTED_CONTENT_TYPE");
        };
    }
}
