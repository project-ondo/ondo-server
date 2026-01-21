package project.team.ondo.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.global.entity.BaseEntity;

@Entity
@Table(
        name = "chat_room_member",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_chat_room_member", columnNames = {"room_id", "user_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoomMemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean blocked;

    @Column(name = "last_read_message_id")
    private Long lastReadMessageId;

    @Column(nullable = false)
    private boolean muted;

    public static ChatRoomMemberEntity create(Long roomId, Long userId) {
        return ChatRoomMemberEntity.builder()
                .roomId(roomId)
                .userId(userId)
                .active(true)
                .blocked(false)
                .lastReadMessageId(null)
                .muted(false)
                .build();
    }

    public void leave() {
        this.active = false;
    }

    public void join() {
        this.active = true;
    }

    public void block() {
        this.blocked = true;
    }

    public void unblock() {
        this.blocked = false;
    }

    public void mute() {
        this.muted = true;
    }

    public void unmute() {
        this.muted = false;
    }

    public void updateLastReadMessageId(Long messageId) {
        this.lastReadMessageId = messageId;
    }
}
