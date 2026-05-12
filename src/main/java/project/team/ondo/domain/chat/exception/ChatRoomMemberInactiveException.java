package project.team.ondo.domain.chat.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class ChatRoomMemberInactiveException extends CustomException {
    public ChatRoomMemberInactiveException() {
        super(ErrorCode.CHAT_MEMBER_INACTIVE);
    }
}
