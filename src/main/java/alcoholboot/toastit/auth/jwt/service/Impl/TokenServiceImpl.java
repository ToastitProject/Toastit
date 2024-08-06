package alcoholboot.toastit.auth.jwt.service.Impl;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.entity.TokenEntity;
import alcoholboot.toastit.auth.jwt.repository.TokenRepository;
import alcoholboot.toastit.auth.jwt.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public void saveOrUpdate(Token token) {
        Token oldToken = tokenRepository.findByUserId(token.getUser().getId())
                .map(TokenEntity::convertToDomain)
                .orElse(null);

        if(oldToken == null) {
            tokenRepository.save(token);
        }

    }

    @Override
    public void deleteByAccessToken(String token) {

    }

    @Override
    public Optional<TokenEntity> findByAccessToken(String token) {
        return Optional.empty();
    }

    @Override
    public Optional<TokenEntity> findByRefreshToken(String token) {
        return Optional.empty();
    }

    @Override
    public void updateByRefreshToken(String refreshToken, String accessToken) {

    }
}
