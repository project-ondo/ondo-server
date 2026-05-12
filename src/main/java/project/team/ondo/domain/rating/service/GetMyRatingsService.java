package project.team.ondo.domain.rating.service;

import project.team.ondo.domain.rating.data.response.UserRatingResponse;
import project.team.ondo.domain.user.entity.UserEntity;
import project.team.ondo.global.response.CursorResponse;

public interface GetMyRatingsService {
    CursorResponse<UserRatingResponse> execute(UserEntity me, Long cursor, int size);
}
