package project.team.ondo.domain.community.bookmark.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.entity.BaseEntity;

@Entity
@Table(
        name = "bookmarks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "post_id"})
        }
)
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookmarkEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    public static BookmarkEntity create(UserEntity user, PostEntity post) {
        return BookmarkEntity.builder()
                .user(user)
                .post(post)
                .build();
    }
}
