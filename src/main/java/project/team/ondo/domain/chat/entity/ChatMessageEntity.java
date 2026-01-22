package project.team.ondo.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.domain.chat.constant.MessageType;
import project.team.ondo.global.entity.BaseEntity;

@Entity
@Table(name = "chat_message", indexes = {
        @Index(name = "idx_chat_message_room_id_id", columnList = "room_id, id"),
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    @Column(nullable = false, length = 2000)
    private String content;

    public static ChatMessageEntity create(Long roomId, Long senderId, MessageType messageType, String content) {
        return ChatMessageEntity.builder()
                .roomId(roomId)
                .senderId(senderId)
                .messageType(messageType)
                .content(content)
                .build();
    }
}
