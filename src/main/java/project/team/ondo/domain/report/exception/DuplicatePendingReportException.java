package project.team.ondo.domain.report.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class DuplicatePendingReportException extends CustomException {
    public DuplicatePendingReportException() {
        super(ErrorCode.DUPLICATE_PENDING_REPORT);
    }
}
