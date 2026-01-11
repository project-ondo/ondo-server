package project.team.ondo.domain.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.service.UserWithdrawService;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

@Service
@RequiredArgsConstructor
public class UserWithdrawServiceImpl implements UserWithdrawService {

    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public void execute() {
        UserEntity user = currentUserProvider.getCurrentUser();
        user.withdraw();
    }
}
