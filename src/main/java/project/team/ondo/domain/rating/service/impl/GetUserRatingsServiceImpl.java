package project.team.ondo.domain.rating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.rating.data.response.UserRatingResponse;
import project.team.ondo.domain.rating.entity.UserRatingEntity;
import project.team.ondo.domain.rating.repository.UserRatingRepository;
import project.team.ondo.domain.rating.service.GetUserRatingsService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.domain.user.repository.UserRepository;
import project.team.ondo.global.response.CursorResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserRatingsServiceImpl implements GetUserRatingsService {

    private final UserRepository userRepository;
    private final UserRatingRepository userRatingRepository;

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<UserRatingResponse> execute(UUID userPublicId, Long cursor, int size) {
        UserEntity target = userRepository.getByPublicId(userPublicId);

        List<UserRatingEntity> rows = userRatingRepository.findByRateeId(target.getId(), cursor, size);

        boolean hasNext = rows.size() > size;
        List<UserRatingEntity> page = hasNext ? rows.subList(0, size) : rows;
        Long nextCursor = hasNext ? page.get(page.size() - 1).getId() : null;

        List<UserRatingResponse> items = page.stream()
                .map(e -> new UserRatingResponse(e.getId(), e.getStars(), e.getComment(), e.getCreatedAt()))
                .toList();

        return CursorResponse.of(items, nextCursor, hasNext);
    }
}
