package project.team.ondo.domain.rating.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class UserAlreadyRatedException extends CustomException {
    public UserAlreadyRatedException() {
        super(ErrorCode.ALREADY_RATED);
    }
}
