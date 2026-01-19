package project.team.ondo.domain.community.comment.service;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.team.ondo.domain.community.comment.data.response.CommentItemResponse;

public interface GetCommentsService {
    Page<@NonNull CommentItemResponse> execute(Long postId, Pageable pageable);
}
