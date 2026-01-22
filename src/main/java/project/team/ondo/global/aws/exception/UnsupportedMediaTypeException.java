package project.team.ondo.global.aws.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class UnsupportedMediaTypeException extends CustomException {
    public UnsupportedMediaTypeException() {
        super(ErrorCode.UNSUPPORTED_MEDIA_TYPE);
    }
}
