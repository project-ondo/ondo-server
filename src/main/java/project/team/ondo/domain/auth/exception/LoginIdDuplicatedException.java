package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class LoginIdDuplicatedException extends CustomException {
    public LoginIdDuplicatedException() {
        super(ErrorCode.LOGIN_ID_DUPLICATED);
    }
}
