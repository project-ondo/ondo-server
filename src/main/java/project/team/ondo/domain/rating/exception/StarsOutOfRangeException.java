package project.team.ondo.domain.rating.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class StarsOutOfRangeException extends CustomException {
    public StarsOutOfRangeException() {
        super(ErrorCode.STARS_OUT_OF_RANGE);
    }
}
