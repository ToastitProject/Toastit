package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.type.Authority;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 사용자 정보를 담는 도메인 클래스.
 * 사용자 정보를 관리하고, 이를 엔티티로 변환할 수 있는 메서드를 제공합니다.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Setter
    private Long id;              // 사용자 ID
    @Setter
    private String email;         // 사용자 이메일
    @Setter
    private String nickname;      // 사용자 닉네임
    @Setter
    private String password;      // 사용자 비밀번호
    private Authority authority;  // 사용자 권한 (ADMIN, USER 등)
    @Setter
    private String profileImageUrl; // 사용자 프로필 이미지 URL
    private String providerType;  // 소셜 로그인 제공자 타입
    private LocalDateTime createDate;  // 계정 생성 날짜

    /**
     * 사용자의 닉네임, 비밀번호, 프로필 이미지 URL, 제공자 타입을 업데이트합니다.
     *
     * @param nickname 새로운 닉네임
     * @param password 새로운 비밀번호
     * @param profileImageUrl 새로운 프로필 이미지 URL
     * @param providerType 소셜 로그인 제공자 타입
     */
    public void update(String nickname, String password, String profileImageUrl, String providerType) {
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.providerType = providerType;
    }

    /**
     * User 도메인 객체를 UserEntity 엔티티로 변환하는 메서드.
     *
     * @return UserEntity 객체
     */
    public UserEntity convertToEntity() {
        return UserEntity.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .authority(this.authority)
                .profileImageUrl(this.profileImageUrl)
                .providerType(this.providerType)
                .build();
    }

    /**
     * 생성 날짜를 LocalDate 형식으로 반환합니다.
     *
     * @return LocalDate 객체 (null일 경우 null 반환)
     */
    public LocalDate getCreateDate() {
        return this.createDate != null ? this.createDate.toLocalDate() : null;
    }
}