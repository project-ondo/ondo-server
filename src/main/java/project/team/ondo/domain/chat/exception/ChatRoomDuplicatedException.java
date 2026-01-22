package project.team.ondo.domain.chat.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class ChatRoomDuplicatedException extends CustomException {
    public ChatRoomDuplicatedException() {
        super(ErrorCode.CHAT_ROOM_DUPLICATED);
    }
}
