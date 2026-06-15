package project.team.ondo.domain.community.post.data.response;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PostRecommendItemResponseTest {

    @Test
    void includesAuthorPublicId() {
        UUID authorPublicId = UUID.randomUUID();

        PostRecommendItemResponse response = new PostRecommendItemResponse(
                1L,
                authorPublicId,
                "제목",
                "작성자",
                List.of("태그"),
                1L,
                2L,
                3L,
                4L,
                null
        );

        assertThat(response.userPublicId()).isEqualTo(authorPublicId);
    }
}
