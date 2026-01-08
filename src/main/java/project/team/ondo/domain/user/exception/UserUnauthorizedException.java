package project.team.ondo.domain.user.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class UserUnauthorizedException extends CustomException {
    public UserUnauthorizedException() {
        super(ErrorCode.USER_UNAUTHORIZED);
    }
}
