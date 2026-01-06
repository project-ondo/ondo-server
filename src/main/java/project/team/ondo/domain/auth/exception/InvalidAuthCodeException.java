package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class InvalidAuthCodeException extends CustomException {
    public InvalidAuthCodeException() {
        super(ErrorCode.INVALID_VERIFICATION_CODE);
    }
}
