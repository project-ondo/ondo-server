package project.team.ondo.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRatingStats {

    @Getter
    @Column(nullable = false)
    private long ratingCount;

    @Getter
    @Column(nullable = false)
    private long ratingSum;

    public static UserRatingStats zero() {
        return new UserRatingStats(0L, 0L);
    }

    public void apply(int stars) {
        this.ratingCount++;
        this.ratingSum += stars;
    }

    public double avg() {
        return ratingCount == 0 ? 0.0 : (double) ratingSum / ratingCount;
    }
}
