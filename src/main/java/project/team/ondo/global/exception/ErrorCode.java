package project.team.ondo.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //AUTH
    LOGIN_ID_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 로그인 ID입니다."),
    DISPLAY_NAME_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    INVALID_EMAIL_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 이메일 인증 토큰입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인에 실패했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    //USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "유저 인증에 실패했습니다."),

    //MAIL
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 코드입니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "인증 코드가 만료되었습니다."),
    ATTEMPT_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "인증 시도 횟수를 초과했습니다."),
    EMAIL_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "이미 인증된 이메일입니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "이메일 인증이 필요합니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),

    //CHAT
    CHAT_ROOM_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방 멤버를 찾을 수 없습니다."),
    USER_CHAT_BLOCKED(HttpStatus.FORBIDDEN, "차단된 유저와는 채팅할 수 없습니다."),
    CHAT_ROOM_DUPLICATED(HttpStatus.CONFLICT, "이미 존재하는 채팅방입니다."),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."),

    //POST
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),

    //COMMENT
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),

    //AWS
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 타입입니다.");

    private final HttpStatus status;
    private final String message;
}
