package alcoholboot.toastit.auth.jwt.service;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.entity.TokenEntity;

import java.util.Optional;

public interface TokenService {
    void saveOrUpdate(Token token);

    void deleteByAccessToken(String token);

    Optional<TokenEntity> findByAccessToken(String token);

    Optional<TokenEntity> findByRefreshToken(String token);

    void updateByRefreshToken(String refreshToken, String accessToken);
}
