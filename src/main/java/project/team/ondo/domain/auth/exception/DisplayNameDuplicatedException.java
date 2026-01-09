package project.team.ondo.domain.auth.exception;

import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

public class DisplayNameDuplicatedException extends CustomException {
    public DisplayNameDuplicatedException() {
        super(ErrorCode.DISPLAY_NAME_DUPLICATED);
    }
}
