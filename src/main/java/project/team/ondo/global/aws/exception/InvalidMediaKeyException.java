package project.team.ondo.global.aws.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class InvalidMediaKeyException extends CustomException {
    public InvalidMediaKeyException() {
        super(ErrorCode.INVALID_MEDIA_KEY);
    }
}