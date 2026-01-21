package project.team.ondo.domain.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    CHAT_MESSAGE("새 메시지", "메시지가 도착했습니다."),
    MATCH_CREATED("매칭 완료", "매칭이 성사되었습니다."),
    POST_COMMENT("새 댓글", "새로운 댓글이 달렸습니다."),
    POST_LIKE("새 좋아요", "게시물이 좋아요를 받았습니다.");

    private final String title;
    private final String body;
}
