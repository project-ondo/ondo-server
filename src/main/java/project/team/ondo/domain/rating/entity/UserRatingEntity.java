package project.team.ondo.domain.rating.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.global.entity.BaseEntity;
import project.team.ondo.global.jpa.StringListJsonConverter;

import java.util.List;

@Entity
@Table(
        name = "user_rating",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_rating_room_rater_ratee",
                        columnNames = {"room_id", "rater_id", "ratee_id"}
                )
        },
        indexes = {
                @Index(name = "idx_user_rating_ratee_id", columnList = "ratee_id"),
                @Index(name = "idx_user_rating_room_id", columnList = "room_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserRatingEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "rater_id", nullable = false)
    private Long raterId;

    @Column(name = "ratee_id", nullable = false)
    private Long rateeId;

    @Column(nullable = false)
    private int stars;

    @Column(length = 500)
    private String comment;

    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "tags", nullable = false, columnDefinition = "JSON")
    private List<String> tags;

    public static UserRatingEntity create(Long roomId, Long raterId, Long rateeId, int stars, String comment, List<String> tags) {
        return UserRatingEntity.builder()
                .roomId(roomId)
                .raterId(raterId)
                .rateeId(rateeId)
                .stars(stars)
                .comment(comment)
                .tags(tags)
                .build();
    }
}
