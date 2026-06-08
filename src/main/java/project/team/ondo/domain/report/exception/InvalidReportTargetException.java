package project.team.ondo.domain.report.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class InvalidReportTargetException extends CustomException {
    public InvalidReportTargetException() {
        super(ErrorCode.INVALID_TARGET_ID);
    }
}
