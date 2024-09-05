package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.type.Authority;
import alcoholboot.toastit.global.entity.JpaAuditingFields;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 사용자 정보를 관리하는 엔티티 클래스.
 * 사용자와 관련된 칵테일, 이미지, 팔로우, 좋아요 등 다양한 정보를 관리합니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity extends JpaAuditingFields {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 사용자 고유 ID

    @Column(nullable = false, unique = true)
    private String email;  // 사용자 이메일

    @Setter
    @Column(nullable = false, unique = true)
    private String nickname;  // 사용자 닉네임

    @Setter
    @Column(nullable = false)
    private String password;  // 사용자 비밀번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;  // 사용자 권한 (예: ADMIN, USER)

    @Column(nullable = false, name = "profile_image_url")
    private String profileImageUrl;  // 프로필 이미지 URL

    @Column(name = "provider_type")
    private String providerType;  // 소셜 로그인 제공자 타입

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CraftCocktailEntity> cocktails = new ArrayList<>();  // 사용자가 만든 칵테일 리스트

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ImageEntity> imageEntities = new ArrayList<>();  // 사용자가 올린 이미지 리스트

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LikeEntity> likes = new ArrayList<>();  // 사용자가 좋아요한 칵테일 리스트

    @Setter
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<FollowEntity> following = new ArrayList<>();  // 사용자가 팔로우하는 사용자 리스트

    @Setter
    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
    private List<FollowEntity> followers = new ArrayList<>();  // 사용자를 팔로우하는 사용자 리스트

    /**
     * 사용자 정보를 초기화하는 생성자.
     *
     * @param email 사용자 이메일
     * @param nickname 사용자 닉네임
     * @param password 사용자 비밀번호
     * @param authority 사용자 권한
     */
    public UserEntity(String email, String nickname, String password, Authority authority) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.authority = authority;
    }

    /**
     * UserEntity 엔티티를 User 도메인 객체로 변환하는 메서드.
     *
     * @return User 도메인 객체
     */
    public User convertToDomain() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .authority(this.authority)
                .profileImageUrl(this.profileImageUrl)
                .createDate(this.createDate)
                .providerType(this.providerType)
                .build();
    }
}