package project.team.ondo.domain.rating.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class MatchNotEndedException extends CustomException {
    public MatchNotEndedException() {
        super(ErrorCode.MATCH_NOT_ENDED);
    }
}
