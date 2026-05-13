package project.team.ondo.domain.community.bookmark.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class AlreadyBookmarkedException extends CustomException {
    public AlreadyBookmarkedException() {
        super(ErrorCode.ALREADY_BOOKMARKED);
    }
}
