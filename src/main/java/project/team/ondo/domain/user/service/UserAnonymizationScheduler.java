package project.team.ondo.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.repository.ChatMessageOutboxRepository;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.notification.repository.NotificationRepository;
import project.team.ondo.domain.rating.repository.UserRatingRepository;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.fcm.repository.UserFcmTokenRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAnonymizationScheduler {

    private static final int GRACE_PERIOD_DAYS = 30;

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageOutboxRepository chatMessageOutboxRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRatingRepository userRatingRepository;
    private final NotificationRepository notificationRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void anonymizeWithdrawnUsers() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(GRACE_PERIOD_DAYS);
        List<UserEntity> targets = userRepository.findAllByStatusAndDeletedAtBefore(UserStatus.DELETED, threshold);

        log.info("탈퇴 익명화 배치 시작: 대상 {} 명", targets.size());
        for (UserEntity user : targets) {
            try {
                anonymizeUser(user);
            } catch (Exception e) {
                log.error("익명화 실패 userId={}", user.getId(), e);
            }
        }
        log.info("탈퇴 익명화 배치 완료");
    }

    private void anonymizeUser(UserEntity user) {
        long userId = user.getId();
        UUID userPublicId = user.getPublicId();

        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAllByUserId(userId);
        if (!chatRooms.isEmpty()) {
            List<UUID> roomPublicIds = chatRooms.stream().map(ChatRoomEntity::getPublicId).toList();
            List<Long> roomIds = chatRooms.stream().map(ChatRoomEntity::getId).toList();

            for (UUID roomPublicId : roomPublicIds) {
                chatMessageOutboxRepository.deleteAllByRoomPublicId(roomPublicId);
            }
            for (Long roomId : roomIds) {
                chatMessageRepository.deleteAllByRoomId(roomId);
                chatRoomMemberRepository.deleteAllByRoomId(roomId);
            }
            chatRoomRepository.deleteAllByUserId(userId);
        }

        userRatingRepository.deleteAllByUserId(userId);
        notificationRepository.deleteByReceiverPublicId(userPublicId);
        userFcmTokenRepository.deleteAllByUserPublicId(userPublicId);

        user.anonymize();
    }
}