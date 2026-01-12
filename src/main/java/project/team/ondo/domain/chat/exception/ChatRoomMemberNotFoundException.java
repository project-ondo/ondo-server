package project.team.ondo.domain.chat.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class ChatRoomMemberNotFoundException extends CustomException {
    public ChatRoomMemberNotFoundException() {
        super(ErrorCode.CHAT_ROOM_MEMBER_NOT_FOUND);
    }
}
