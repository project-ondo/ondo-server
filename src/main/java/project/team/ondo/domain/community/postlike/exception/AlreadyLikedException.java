package project.team.ondo.domain.community.postlike.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class AlreadyLikedException extends CustomException {
    public AlreadyLikedException() {
        super(ErrorCode.ALREADY_LIKED);
    }
}
