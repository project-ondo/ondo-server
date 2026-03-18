package project.team.ondo.domain.chat.data;

public record ChatRoomInfo(Long roomId, Long userAId, Long userBId, boolean matchEnded) {}
