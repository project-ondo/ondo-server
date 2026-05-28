package project.team.ondo.domain.report.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class ReportTargetNotFoundException extends CustomException {
    public ReportTargetNotFoundException() {
        super(ErrorCode.REPORT_TARGET_NOT_FOUND);
    }
}
