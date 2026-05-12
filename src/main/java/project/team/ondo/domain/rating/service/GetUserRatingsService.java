package project.team.ondo.domain.rating.service;

import project.team.ondo.domain.rating.data.response.UserRatingResponse;
import project.team.ondo.global.response.CursorResponse;

import java.util.UUID;

public interface GetUserRatingsService {
    CursorResponse<UserRatingResponse> execute(UUID userPublicId, Long cursor, int size);
}
