package project.team.ondo.global.fcm.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.global.entity.BaseEntity;

import java.util.UUID;

@Entity
@Table(
        name = "user_fcm_token",
        indexes = {
                @Index(name = "idx_user_fcm_token_user_active", columnList = "user_public_id, active")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_fcm_token_token", columnNames = {"token"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserFcmTokenEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID userPublicId;

    @Column(length = 512, nullable = false)
    private String token;

    @Column(nullable = false)
    private boolean active;

    public static UserFcmTokenEntity create(
            UUID userPublicId,
            String token
    ) {
        return UserFcmTokenEntity.builder()
                .userPublicId(userPublicId)
                .token(token)
                .active(true)
                .build();
    }

    public void deactivate() {
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }

    public void changeOwner(UUID userPublicId) {
        this.userPublicId = userPublicId;
    }
}
