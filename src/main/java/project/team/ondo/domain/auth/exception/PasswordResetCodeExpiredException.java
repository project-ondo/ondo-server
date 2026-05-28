package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class PasswordResetCodeExpiredException extends CustomException {
    public PasswordResetCodeExpiredException() {
        super(ErrorCode.PASSWORD_RESET_CODE_EXPIRED);
    }
}