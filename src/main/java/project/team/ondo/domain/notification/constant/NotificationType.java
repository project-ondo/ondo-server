package project.team.ondo.domain.notification.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    CHAT_MESSAGE("새 메시지", "메시지가 도착했습니다."),
    MATCH_CREATED("매칭 완료", "매칭이 성사되었습니다."),
    POST_COMMENT("새 댓글", "새로운 댓글이 달렸습니다."),
    RATE_REQUEST("평가 요청", "이번 매칭에 대한 별점을 남겨주세요."),
    POST_LIKE("새 좋아요", "게시물이 좋아요를 받았습니다."),
    CHAT_MEMBER_LEFT("상대방 퇴장", "상대방이 채팅방을 나갔습니다."),
    REPORT_RECEIVED("신고 접수", "신고가 접수되었습니다."),
    REPORT_RESOLVED("신고 처리 완료", "신고가 처리되었습니다."),
    REPORT_CONTENT_DELETED("콘텐츠 삭제", "회원님의 콘텐츠가 운영 정책 위반으로 삭제되었습니다."),
    USER_SUSPENDED("계정 정지", "운영 정책 위반으로 계정이 일시 정지되었습니다.");

    private final String title;
    private final String body;
}
