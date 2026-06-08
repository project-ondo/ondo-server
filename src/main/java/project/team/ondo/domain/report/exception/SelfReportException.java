package project.team.ondo.domain.report.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class SelfReportException extends CustomException {
    public SelfReportException() {
        super(ErrorCode.SELF_REPORT);
    }
}