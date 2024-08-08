package alcoholboot.toastit.auth.jwt.service;

import alcoholboot.toastit.auth.jwt.domain.Token;

public interface TokenService {
    void saveOrUpdate(Token token);
    void deleteByAccessToken(String token);
    void updateByRefreshToken(String refreshToken, String accessToken);
}