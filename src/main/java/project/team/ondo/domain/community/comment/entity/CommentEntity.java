package project.team.ondo.domain.community.comment.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.domain.community.comment.constant.CommentStatus;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.entity.BaseEntity;

@Entity
@Table(name = "comments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommentStatus status;

    public static CommentEntity create(
            String content,
            PostEntity post,
            UserEntity author
    ) {
        return CommentEntity.builder()
                .content(content)
                .post(post)
                .author(author)
                .status(CommentStatus.ACTIVE)
                .build();
    }

    public void delete() {
        this.status = CommentStatus.DELETED;
    }
}
