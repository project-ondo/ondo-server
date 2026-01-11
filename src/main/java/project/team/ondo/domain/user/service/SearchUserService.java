package project.team.ondo.domain.user.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.user.data.request.UserSearchCondition;
import project.team.ondo.domain.user.data.response.UserSearchItemResponse;

public interface SearchUserService {
    Page<@NonNull UserSearchItemResponse> execute(UserSearchCondition condition, Pageable pageable);
}
