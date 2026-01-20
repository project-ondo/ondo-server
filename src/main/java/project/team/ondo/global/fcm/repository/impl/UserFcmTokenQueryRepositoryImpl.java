package project.team.ondo.global.fcm.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.team.ondo.global.fcm.entity.QUserFcmTokenEntity;
import project.team.ondo.global.fcm.repository.UserFcmTokenQueryRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserFcmTokenQueryRepositoryImpl implements UserFcmTokenQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QUserFcmTokenEntity fcmToken = QUserFcmTokenEntity.userFcmTokenEntity;

    @Override
    public List<String> findActiveTokens(UUID userPublicId) {
        return jpaQueryFactory
                .select(fcmToken.token)
                .from(fcmToken)
                .where(
                        fcmToken.userPublicId.eq(userPublicId),
                        fcmToken.active.isTrue()
                )
                .fetch();
    }

    @Override
    public long deactivateToken(UUID userPublicId, String token) {
        return jpaQueryFactory
                .update(fcmToken)
                .set(fcmToken.active, false)
                .where(
                        fcmToken.userPublicId.eq(userPublicId),
                        fcmToken.token.eq(token),
                        fcmToken.active.isTrue()
                )
                .execute();
    }

    @Override
    public long deactivateAll(UUID userPublicId) {
        return jpaQueryFactory
                .update(fcmToken)
                .set(fcmToken.active, false)
                .where(
                        fcmToken.userPublicId.eq(userPublicId),
                        fcmToken.active.isTrue()
                )
                .execute();
    }
}
