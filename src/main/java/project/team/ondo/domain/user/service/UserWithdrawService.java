package project.team.ondo.domain.user.service;

import project.team.ondo.domain.user.entity.UserEntity;

public interface UserWithdrawService {
    void execute(UserEntity me);
}
