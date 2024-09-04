package alcoholboot.toastit.infra.jwt.domain;

import alcoholboot.toastit.infra.jwt.entity.TokenEntity;
import alcoholboot.toastit.feature.user.domain.User;
import lombok.*;

/**
 * 토큰 정보 클래스
 * 사용자의 액세스 토큰, 리프레시 토큰, 그리고 그랜트 타입을 관리한다.
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    // 토큰 식별자
    private Long id;

    // 회원 도메인
    private User user;

    // 액세스 토큰
    @Setter
    private String accessToken;

    // 리프레시 토큰
    private String refreshToken;

    // 토큰 인증 타입
    private String grantType;

    /**
     * 기존의 토큰 정보를 업데이트
     *
     * @param accessToken 업데이트할 액세스 토큰
     * @param refreshToken 업데이트할 리프레시 토큰
     * @param grantType 업데이트할 그랜트 타입
     */
    public void update(String accessToken, String refreshToken, String grantType) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.grantType = grantType;
    }

    /**
     * 현재 토큰 객체를 엔티티 객체로 변환한다.
     *
     * @return 토큰 엔티티 객체
     */
    public TokenEntity convertToEntity() {
        return TokenEntity.builder()
                .id(this.id)
                .userEntity(this.user.convertToEntity())
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .grantType(this.grantType)
                .build();
    }
}