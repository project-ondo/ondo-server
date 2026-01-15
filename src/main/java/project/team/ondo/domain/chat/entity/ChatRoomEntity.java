package project.team.ondo.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.global.entity.BaseEntity;

import java.util.UUID;

@Entity
@Table(
        name = "chat_room",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_chat_room_pair", columnNames = {"user_a_id", "user_b_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoomEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID publicId;

    @Column(name = "user_a_id", nullable = false)
    private Long userAId;

    @Column(name = "user_b_id", nullable = false)
    private Long userBId;

    @PrePersist
    void prePersist() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }

    public static ChatRoomEntity create(Long userAId, Long userBId) {
        long a = Math.min(userAId, userBId);
        long b = Math.max(userAId, userBId);
        return ChatRoomEntity.builder()
                .userAId(a)
                .userBId(b)
                .build();
    }
}
