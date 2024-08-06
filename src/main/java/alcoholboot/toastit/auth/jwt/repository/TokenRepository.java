package alcoholboot.toastit.auth.jwt.repository;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.entity.TokenEntity;

import java.util.Optional;

public interface TokenRepository {
    void save(Token token);
    Optional<TokenEntity> findByUserId(Long userId);
    Optional<TokenEntity> findByAccessToken(String accessToken);
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}