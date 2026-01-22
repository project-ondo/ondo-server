package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class LoginFailedException extends CustomException {
    public LoginFailedException() {
        super(ErrorCode.LOGIN_FAILED);
    }
}
