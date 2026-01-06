package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class AttemptLimitExceededException extends CustomException {
    public AttemptLimitExceededException() {
        super(ErrorCode.ATTEMPT_LIMIT_EXCEEDED);
    }
}
