package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class AuthCodeExpiresException extends CustomException {
    public AuthCodeExpiresException() {
        super(ErrorCode.VERIFICATION_CODE_EXPIRED);
    }
}
