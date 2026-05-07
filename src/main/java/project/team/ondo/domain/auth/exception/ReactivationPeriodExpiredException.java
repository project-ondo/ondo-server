package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class ReactivationPeriodExpiredException extends CustomException {
    public ReactivationPeriodExpiredException() {
        super(ErrorCode.REACTIVATION_PERIOD_EXPIRED);
    }
}