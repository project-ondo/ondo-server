package project.team.ondo.domain.chat.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class UserChatBlockedException extends CustomException {
    public UserChatBlockedException() {
        super(ErrorCode.USER_CHAT_BLOCKED);
    }
}
