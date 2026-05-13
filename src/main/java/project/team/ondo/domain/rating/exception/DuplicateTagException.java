package project.team.ondo.domain.rating.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class DuplicateTagException extends CustomException {
    public DuplicateTagException() {
        super(ErrorCode.DUPLICATE_TAG);
    }
}