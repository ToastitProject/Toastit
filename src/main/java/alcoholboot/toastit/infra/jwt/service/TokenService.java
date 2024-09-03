package alcoholboot.toastit.infra.jwt.service;

import alcoholboot.toastit.infra.jwt.domain.Token;

public interface TokenService {
    void saveOrUpdate(Token token);
    void deleteByAccessToken(String token);
    void updateByRefreshToken(String refreshToken, String accessToken);
}