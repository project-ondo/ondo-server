package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class InvalidEmailVerificationTokenException extends CustomException {
    public InvalidEmailVerificationTokenException() {
        super(ErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN);
    }
}
