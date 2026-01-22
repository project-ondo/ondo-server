package project.team.ondo.domain.rating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.entity.ChatRoomEntity;
import project.team.ondo.domain.chat.entity.ChatRoomMemberEntity;
import project.team.ondo.domain.chat.exception.ChatRoomMemberNotFoundException;
import project.team.ondo.domain.chat.exception.ChatRoomNotFoundException;
import project.team.ondo.domain.chat.repository.ChatRoomMemberRepository;
import project.team.ondo.domain.chat.repository.ChatRoomRepository;
import project.team.ondo.domain.rating.entity.UserRatingEntity;
import project.team.ondo.domain.rating.exception.MatchNotEndedException;
import project.team.ondo.domain.rating.exception.StarsOutOfRangeException;
import project.team.ondo.domain.rating.exception.UserAlreadyRatedException;
import project.team.ondo.domain.rating.repository.UserRatingRepository;
import project.team.ondo.domain.rating.service.RateUserService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.security.jwt.service.CurrentUserProvider;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateUserServiceImpl implements RateUserService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final UserRepository userRepository;
    private final UserRatingRepository userRatingRepository;
    private final CurrentUserProvider currentUserProvider;

    @Transactional
    @Override
    public void execute(UUID chatRoomPublicId, int stars, String comment) {
        if (stars < 1 || stars > 5) {
            throw new StarsOutOfRangeException();
        }

        UserEntity me = currentUserProvider.getCurrentUser();

        ChatRoomEntity room = chatRoomRepository.findByPublicId(chatRoomPublicId)
                .orElseThrow(ChatRoomNotFoundException::new);

        chatRoomMemberRepository.findByRoomIdAndUserId(room.getId(), me.getId())
                .orElseThrow(ChatRoomMemberNotFoundException::new);

        List<ChatRoomMemberEntity> members = chatRoomMemberRepository.findAllByRoomId(room.getId());
        if (members.size() < 2 || members.stream().anyMatch(ChatRoomMemberEntity::isActive)) {
            throw new MatchNotEndedException();
        }

        Long opponentId = room.getUserAId().equals(me.getId()) ? room.getUserBId() : room.getUserAId();

        if (userRatingRepository.existsByRoomIdAndRaterIdAndRateeId(room.getId(), me.getId(), opponentId)) {
            throw new UserAlreadyRatedException();
        }

        userRatingRepository.save(
                UserRatingEntity.create(room.getId(), me.getId(), opponentId, stars, comment)
        );

        UserEntity opponent = userRepository.findById(opponentId).orElseThrow(UserNotFoundException::new);
        opponent.applyNewRating(stars);
    }
}
