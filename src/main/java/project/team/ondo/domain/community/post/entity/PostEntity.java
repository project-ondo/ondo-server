package project.team.ondo.domain.community.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.access.AccessDeniedException;
import project.team.ondo.domain.community.post.constant.PostStatus;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity author;

    @Enumerated(EnumType.STRING)
    private PostStatus status;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private long likeCount;

    @Column(nullable = false)
    private long commentCount;

    public static PostEntity create(
            String title,
            String content,
            UserEntity author,
            List<String> tags
    ) {
        return PostEntity.builder()
                .title(title)
                .content(content)
                .author(author)
                .status(PostStatus.ACTIVE)
                .tags(tags != null ? tags : new ArrayList<>())
                .viewCount(0L)
                .likeCount(0L)
                .commentCount(0L)
                .build();
    }

    public void update(String title, String content, List<String> tags) {
        this.title = title;
        this.content = content;
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public void requireAuthor(UUID callerPublicId) {
        if (!this.author.getPublicId().equals(callerPublicId))
            throw new AccessDeniedException("게시글 수정/삭제 권한이 없습니다.");
    }

    public void delete() {
        this.status = PostStatus.DELETED;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        if (this.commentCount > 0) {
            this.commentCount--;
        }
    }
}
