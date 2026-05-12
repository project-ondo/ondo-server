package project.team.ondo.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.repository.ChatMessageOutboxRepository;
import project.team.ondo.domain.chat.repository.ChatMessageRepository;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.notification.repository.NotificationRepository;
import project.team.ondo.domain.rating.repository.UserRatingRepository;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.fcm.repository.UserFcmTokenRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserAnonymizationService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageOutboxRepository chatMessageOutboxRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRatingRepository userRatingRepository;
    private final NotificationRepository notificationRepository;
    private final UserFcmTokenRepository userFcmTokenRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void anonymizeUser(UserEntity user) {
        long userId = user.getId();
        UUID userPublicId = user.getPublicId();

        List<ChatRoomEntity> chatRooms = chatRoomRepository.findAllByUserId(userId);
        if (!chatRooms.isEmpty()) {
            List<UUID> roomPublicIds = chatRooms.stream().map(ChatRoomEntity::getPublicId).toList();
            List<Long> roomIds = chatRooms.stream().map(ChatRoomEntity::getId).toList();

            chatMessageOutboxRepository.deleteAllByRoomPublicIdIn(roomPublicIds);
            chatMessageRepository.deleteAllByRoomIdIn(roomIds);
            chatRoomMemberRepository.deleteAllByRoomIdIn(roomIds);
            chatRoomRepository.deleteAllByUserId(userId);
        }

        userRatingRepository.deleteAllByUserId(userId);
        notificationRepository.deleteByReceiverPublicId(userPublicId);
        userFcmTokenRepository.deleteAllByUserPublicId(userPublicId);

        user.anonymize();
    }
}
