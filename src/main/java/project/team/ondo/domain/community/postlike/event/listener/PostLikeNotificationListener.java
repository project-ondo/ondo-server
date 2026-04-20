package project.team.ondo.domain.community.postlike.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.exception.PostNotFoundException;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.community.postlike.event.PostLikedEvent;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.data.NotificationResult;
import project.team.ondo.domain.notification.service.CreateNotificationService;
import project.team.ondo.domain.notification.service.NotificationPolicyService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.fcm.data.command.FcmPushCommand;
import project.team.ondo.global.fcm.service.FcmPushService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PostLikeNotificationListener {

    private final FcmPushService fcmPushService;
    private final CreateNotificationService createNotificationService;

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationPolicyService notificationPolicyService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PostLikedEvent event) {

        UserEntity actor = userRepository.getByPublicId(event.actorPublicId());
        String actorDisplayName = actor.getDisplayName();

        PostEntity post = postRepository.findById(event.postId()).orElseThrow(PostNotFoundException::new);
        String postTitle = post.getTitle();

        String target = "postId=" + event.postId();

        String title = "새 좋아요 알림";
        String singleBody = actorDisplayName + "님이 " + postTitle + " 게시글을 좋아합니다.";
        String aggregatedBodyPrefix = actorDisplayName + "님 외 {n}명이 " + postTitle + " 게시글을 좋아합니다.";


        NotificationResult saved = createNotificationService.createOrAggregate(
                event.receiverPublicId(),
                NotificationType.POST_LIKE,
                target,
                title,
                singleBody,
                aggregatedBodyPrefix
        );

        if (!notificationPolicyService.shouldSendPush(event.receiverPublicId(), NotificationType.POST_LIKE)) return;

        Map<String, String> data = new HashMap<>();
        data.put("type", "POST_LIKE");
        data.put("postId", String.valueOf(event.postId()));
        data.put("actorPublicId", event.actorPublicId().toString());
        data.put("notificationId", String.valueOf(saved.id()));
        data.put("groupCount", String.valueOf(saved.groupCount()));

        fcmPushService.send(
                new FcmPushCommand(
                        event.receiverPublicId(),
                        title,
                        saved.body(),
                        Map.copyOf(data)
                )
        );
    }
}
