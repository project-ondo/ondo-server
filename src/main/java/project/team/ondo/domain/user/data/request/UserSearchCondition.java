package project.team.ondo.domain.user.data.request;

import java.util.List;

public record UserSearchCondition(
        String keyword,
        String major,
        List<String> interests,
        String sort
) {
}
