package alcoholboot.toastit.auth.jwt.repository;

import alcoholboot.toastit.auth.jwt.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * 토큰 엔티티에 대한 데이터 접근 레이어를 정의하는 인터페이스
 * JpaRepository를 상속받아 기본적인 CRUD 연산과 추가적인 토큰 관련 검색 기능을 제공한다.
 */
public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    /**
     * 특정 사용자의 ID를 기반으로 토큰 엔티티를 검색한다.
     *
     * @param userId 사용자 고유 식별자
     * @return 해당 사용자의 토큰 엔티티를 감싸는 Optional 객체
     */
    Optional<TokenEntity> findByUserEntityId(Long userId);

    /**
     * 액세스 토큰을 기반으로 토큰 엔티티를 검색한다.
     *
     * @param accessToken 검색할 액세스 토큰
     * @return 해당 액세스 토큰에 해당하는 토큰 엔티티를 감싸는 Optional 객체
     */
    Optional<TokenEntity> findByAccessToken(String accessToken);

    /**
     * 리프레시 토큰을 기반으로 토큰 엔티티를 검색한다.
     *
     * @param refreshToken 검색할 리프레시 토큰
     * @return 해당 리프레시 토큰에 해당하는 토큰 엔티티를 감싸는 Optional 객체
     */
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}