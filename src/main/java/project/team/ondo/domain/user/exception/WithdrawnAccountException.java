package project.team.ondo.domain.user.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class WithdrawnAccountException extends CustomException {
    public WithdrawnAccountException() {
        super(ErrorCode.WITHDRAWN_ACCOUNT);
    }
}