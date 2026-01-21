package project.team.ondo.domain.community.comment.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import project.team.ondo.domain.community.comment.event.PostCommentCreatedEvent;
import project.team.ondo.domain.community.post.entity.PostEntity;
import project.team.ondo.domain.community.post.exception.PostNotFoundException;
import project.team.ondo.domain.community.post.repository.PostRepository;
import project.team.ondo.domain.notification.constant.NotificationType;
import project.team.ondo.domain.notification.entity.NotificationEntity;
import project.team.ondo.domain.notification.repository.NotificationRepository;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.fcm.data.command.FcmPushCommand;
import project.team.ondo.global.fcm.service.FcmPushService;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PostCommentNotificationListener {

    private final NotificationRepository notificationRepository;
    private final FcmPushService fcmPushService;

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PostCommentCreatedEvent event) {
        UserEntity actor = userRepository.findByPublicId(event.actorPublicId()).orElseThrow(UserNotFoundException::new);
        String actorDisplayName = actor.getDisplayName();

        PostEntity post = postRepository.findById(event.postId()).orElseThrow(PostNotFoundException::new);
        String postTitle = post.getTitle();

        String title = "새 댓글 알림";
        String body = actorDisplayName + "님이 '" + postTitle + "' 게시글에 댓글을 남겼습니다.";

        notificationRepository.save(
                NotificationEntity.create(
                        event.receiverPublicId(),
                        NotificationType.POST_COMMENT,
                        title,
                        body,
                        "postId=" + event.postId()
                )
        );

        Map<String, String> data = new HashMap<>();
        data.put("type", "POST_COMMENT");
        data.put("postId", String.valueOf(event.postId()));
        data.put("commentId", String.valueOf(event.commentId()));
        data.put("actorPublicId", event.actorPublicId().toString());
        if (postTitle != null && !postTitle.isBlank()) data.put("postTitle", postTitle);

        fcmPushService.send(
                new FcmPushCommand(
                        event.receiverPublicId(),
                        title,
                        body,
                        Map.copyOf(data)
                )
        );
    }
}
