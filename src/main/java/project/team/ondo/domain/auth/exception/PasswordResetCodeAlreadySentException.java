package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class PasswordResetCodeAlreadySentException extends CustomException {
    public PasswordResetCodeAlreadySentException() {
        super(ErrorCode.PASSWORD_RESET_CODE_ALREADY_SENT);
    }
}