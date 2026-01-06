package project.team.ondo.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import project.team.ondo.domain.user.constant.Gender;
import project.team.ondo.domain.user.constant.UserRole;
import project.team.ondo.domain.user.constant.UserStatus;
import project.team.ondo.global.entity.BaseEntity;
import project.team.ondo.global.jpa.UuidBinaryConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = UuidBinaryConverter.class)
    @Column(nullable = false, unique = true, columnDefinition = "BINARY(16)")
    private UUID publicId;

    @Column(nullable = false, unique = true, length = 20)
    private String loginId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String displayName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false, length = 20)
    private String major;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "user_interests", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "interest", nullable = false, length = 30)
    private List<String> interests = new ArrayList<>();

    @Column(length = 50)
    private String profileImageUrl;

    @Column(length = 500)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    @PrePersist
    void prePersist() {
        if (publicId == null) publicId = UUID.randomUUID();
    }

    public static UserEntity create(
            String loginId,
            String email,
            String password,
            String displayName,
            Gender gender,
            String major,
            List<String> interests,
            String profileImageUrl
    ) {
        return UserEntity.builder()
                .loginId(loginId)
                .email(email)
                .password(password)
                .displayName(displayName)
                .gender(gender)
                .major(major)
                .interests(interests != null ? interests : new ArrayList<>())
                .profileImageUrl(profileImageUrl)
                .bio("")
                .role(UserRole.ROLE_USER)
                .status(UserStatus.ACTIVE)
                .build();
    }

}
