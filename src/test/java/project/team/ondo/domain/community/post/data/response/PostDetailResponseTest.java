package project.team.ondo.domain.community.post.data.response;

import org.junit.jupiter.api.Test;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.entity.UserEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PostDetailResponseTest {

    @Test
    void fromIncludesAuthorPublicId() {
        UUID authorPublicId = UUID.randomUUID();
        UserEntity author = UserEntity.builder()
                .publicId(authorPublicId)
                .loginId("author")
                .email("author@example.com")
                .password("password")
                .displayName("작성자")
                .major("컴퓨터공학")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .build();
        PostEntity post = PostEntity.create("제목", "내용", author, List.of("태그"));

        PostDetailResponse response = PostDetailResponse.from(post);

        assertThat(response.userPublicId()).isEqualTo(authorPublicId);
    }
}
