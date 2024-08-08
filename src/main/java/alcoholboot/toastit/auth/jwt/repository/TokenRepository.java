package alcoholboot.toastit.auth.jwt.repository;

import alcoholboot.toastit.auth.jwt.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    TokenEntity findByUserEntityId(Long userId);
    Optional<TokenEntity> findByAccessToken(String accessToken);
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}