package project.team.ondo.domain.community.postlike.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.entity.BaseEntity;

@Entity
@Table(
        name = "post_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostLikeEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    public static PostLikeEntity create(UserEntity user, PostEntity post) {
        return PostLikeEntity.builder()
                .user(user)
                .post(post)
                .build();

    }
}
