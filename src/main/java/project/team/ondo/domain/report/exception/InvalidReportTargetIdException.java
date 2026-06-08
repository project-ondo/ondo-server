package project.team.ondo.domain.report.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class InvalidReportTargetIdException extends CustomException {
    public InvalidReportTargetIdException() {
        super(ErrorCode.INVALID_REPORT_TARGET_ID);
    }
}
