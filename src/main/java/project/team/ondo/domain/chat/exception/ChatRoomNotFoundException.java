package project.team.ondo.domain.chat.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class ChatRoomNotFoundException extends CustomException {
    public ChatRoomNotFoundException() {
        super(ErrorCode.CHAT_ROOM_NOT_FOUND);
    }
}
