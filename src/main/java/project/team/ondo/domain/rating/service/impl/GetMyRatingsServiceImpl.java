package project.team.ondo.domain.rating.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.team.ondo.domain.rating.data.response.UserRatingResponse;
import project.team.ondo.domain.rating.entity.UserRatingEntity;
import project.team.ondo.domain.rating.repository.UserRatingRepository;
import project.team.ondo.domain.rating.service.GetMyRatingsService;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.response.CursorResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetMyRatingsServiceImpl implements GetMyRatingsService {

    private final UserRatingRepository userRatingRepository;

    @Override
    @Transactional(readOnly = true)
    public CursorResponse<UserRatingResponse> execute(UserEntity me, Long cursor, int size) {
        List<UserRatingEntity> rows = userRatingRepository.findByRateeId(me.getId(), cursor, size);

        boolean hasNext = rows.size() > size;
        List<UserRatingEntity> page = hasNext ? rows.subList(0, size) : rows;
        Long nextCursor = hasNext ? page.get(page.size() - 1).getId() : null;

        List<UserRatingResponse> items = page.stream()
                .map(UserRatingResponse::from)
                .toList();

        return CursorResponse.of(items, nextCursor, hasNext);
    }
}
