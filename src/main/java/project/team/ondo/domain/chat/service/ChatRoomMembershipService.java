package project.team.ondo.domain.chat.service;

import project.team.ondo.domain.chat.data.ChatRoomInfo;

import java.util.UUID;

public interface ChatRoomMembershipService {

    /**
     * 채팅방 존재 여부와 사용자의 멤버십을 검증하고 채팅방 정보를 반환한다.
     * 채팅방이 없으면 ChatRoomNotFoundException, 멤버가 아니면 ChatRoomMemberNotFoundException 발생.
     */
    ChatRoomInfo validateAndGet(UUID roomPublicId, Long userId);
}
