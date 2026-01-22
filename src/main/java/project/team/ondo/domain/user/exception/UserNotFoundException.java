package project.team.ondo.domain.user.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class UserNotFoundException extends CustomException
{
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
