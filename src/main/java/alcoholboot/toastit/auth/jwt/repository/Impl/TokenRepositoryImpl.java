package alcoholboot.toastit.auth.jwt.repository.Impl;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.repository.TokenCrudRepository;
import alcoholboot.toastit.auth.jwt.repository.TokenRepository;
import alcoholboot.toastit.auth.jwt.entity.TokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {
    private final TokenCrudRepository tokenCrudRepository;

    @Override
    public void save(Token token) {
        tokenCrudRepository.save(token.covertToEntity());
    }

    @Override
    public Optional<TokenEntity> findByUserId(Long userId) {
        return tokenCrudRepository.findByUserEntityId(userId);
    }

    @Override
    public Optional<TokenEntity> findByAccessToken(String accessToken) {
        return tokenCrudRepository.findByAccessToken(accessToken);
    }

    @Override
    public Optional<TokenEntity> findByRefreshToken(String refreshToken) {
        return tokenCrudRepository.findByRefreshToken(refreshToken);
    }
}