package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.service.UserWithdrawService;

@Service
@RequiredArgsConstructor
public class UserWithdrawServiceImpl implements UserWithdrawService {

    @Transactional
    @Override
    public void execute(UserEntity me) {
        me.withdraw();
    }
}
