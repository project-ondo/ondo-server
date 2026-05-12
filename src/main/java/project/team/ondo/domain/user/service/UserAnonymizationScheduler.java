package project.team.ondo.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserAnonymizationScheduler {

    private static final int GRACE_PERIOD_DAYS = 30;

    private final UserRepository userRepository;
    private final UserAnonymizationService userAnonymizationService;

    @Scheduled(cron = "0 0 3 * * *")
    public void anonymizeWithdrawnUsers() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(GRACE_PERIOD_DAYS);
        List<UserEntity> targets = userRepository.findAllByStatusAndDeletedAtBefore(UserStatus.DELETED, threshold);

        log.info("탈퇴 익명화 배치 시작: 대상 {} 명", targets.size());
        for (UserEntity user : targets) {
            try {
                userAnonymizationService.anonymizeUser(user);
            } catch (Exception e) {
                log.error("익명화 실패 userId={}", user.getId(), e);
            }
        }
        log.info("탈퇴 익명화 배치 완료");
    }
}