package project.team.ondo.domain.chat.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class ChatOpponentWithdrawnException extends CustomException {
    public ChatOpponentWithdrawnException() {
        super(ErrorCode.CHAT_OPPONENT_WITHDRAWN);
    }
}
