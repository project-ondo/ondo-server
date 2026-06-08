package project.team.ondo.domain.auth.exception;

import lombok.Getter;
import project.team.ondo.global.exception.CustomException;
import project.team.ondo.global.exception.ErrorCode;

import java.time.LocalDateTime;

@Getter
public class UserSuspendedException extends CustomException {
    private final LocalDateTime suspendedUntil;

    public UserSuspendedException(LocalDateTime suspendedUntil) {
        super(ErrorCode.USER_SUSPENDED);
        this.suspendedUntil = suspendedUntil;
    }
}