package project.team.ondo.domain.report.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class ReportAlreadyProcessedException extends CustomException {
    public ReportAlreadyProcessedException() {
        super(ErrorCode.REPORT_ALREADY_PROCESSED);
    }
}