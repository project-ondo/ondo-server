package project.team.ondo.global.fcm.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.global.fcm.data.command.FcmPushCommand;
import project.team.ondo.global.fcm.entity.UserFcmTokenEntity;
import project.team.ondo.global.fcm.repository.UserFcmTokenRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmPushService {

    private final UserFcmTokenRepository userFcmTokenRepository;

    @Transactional
    public void send(FcmPushCommand command) {
        List<String> tokens = userFcmTokenRepository.findActiveTokens(command.receiverUserPublicId());
        if (tokens.isEmpty()) return;

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(command.title())
                                .setBody(command.body())
                                .build()
                )
                .putAllData(command.data() == null ? Map.of() : command.data())
                .build();

        try {
            BatchResponse response = FirebaseMessaging.getInstance().sendEachForMulticast(message);

            List<SendResponse> responses = response.getResponses();
            for (int i = 0; i < responses.size(); i++) {
                SendResponse r = responses.get(i);
                if (r.isSuccessful()) continue;

                FirebaseMessagingException ex = r.getException();
                if (ex == null) continue;

                MessagingErrorCode code = ex.getMessagingErrorCode();
                if (code == MessagingErrorCode.UNREGISTERED
                        || code == MessagingErrorCode.INVALID_ARGUMENT) {
                    String badToken = tokens.get(i);
                    userFcmTokenRepository.findByToken(badToken)
                            .ifPresent(UserFcmTokenEntity::deactivate);
                }
            }
        } catch (FirebaseMessagingException e) {
        }
    }
}
