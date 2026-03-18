package project.team.ondo.domain.rating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.chat.data.ChatRoomInfo;
import project.team.ondo.domain.chat.service.ChatRoomMembershipService;
import project.team.ondo.domain.rating.entity.UserRatingEntity;
import project.team.ondo.domain.rating.exception.MatchNotEndedException;
import project.team.ondo.domain.rating.exception.StarsOutOfRangeException;
import project.team.ondo.domain.rating.exception.UserAlreadyRatedException;
import project.team.ondo.domain.rating.repository.UserRatingRepository;
import project.team.ondo.domain.rating.service.RateUserService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.exception.UserNotFoundException;
import project.team.ondo.domain.user.repository.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RateUserServiceImpl implements RateUserService {

    private final ChatRoomMembershipService chatRoomMembershipService;
    private final UserRepository userRepository;
    private final UserRatingRepository userRatingRepository;

    @Transactional
    @Override
    public void execute(UserEntity me, UUID chatRoomPublicId, int stars, String comment) {
        if (stars < 1 || stars > 5) {
            throw new StarsOutOfRangeException();
        }

        ChatRoomInfo room = chatRoomMembershipService.validateAndGet(chatRoomPublicId, me.getId());

        if (!room.matchEnded()) {
            throw new MatchNotEndedException();
        }

        Long opponentId = room.userAId().equals(me.getId()) ? room.userBId() : room.userAId();

        if (userRatingRepository.existsByRoomIdAndRaterIdAndRateeId(room.roomId(), me.getId(), opponentId)) {
            throw new UserAlreadyRatedException();
        }

        userRatingRepository.save(
                UserRatingEntity.create(room.roomId(), me.getId(), opponentId, stars, comment)
        );

        UserEntity opponent = userRepository.findById(opponentId).orElseThrow(UserNotFoundException::new);
        opponent.applyNewRating(stars);
    }
}
