package alcoholboot.toastit.auth.jwt.service.Impl;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.auth.jwt.entity.TokenEntity;
import alcoholboot.toastit.auth.jwt.repository.TokenRepository;
import alcoholboot.toastit.auth.jwt.service.TokenService;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * {@link TokenService} 인터페이스 구현 클래스
 * JWT 토큰을 관리하는 다양한 메서드를 제공
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    /**
     * 토큰 사용자의 토큰이 저장되어 있을 경우 update, 없을 경우 create
     *
     * @param token Token domain
     */
    @Transactional
    public void saveOrUpdate(Token token) {
        Optional<Token> oldToken =  tokenRepository.findByUserEntityId(token.getUser().getId()).map(TokenEntity::convertToDomain);

        if (oldToken.isEmpty()) {
            tokenRepository.save(token.convertToEntity());
        } else {
            oldToken.get().update(token.getAccessToken(), token.getRefreshToken(), token.getGrantType());

            tokenRepository.save(oldToken.get().convertToEntity());
        }
    }

    /**
     * access token으로 Token 데이터를 가져와 삭제하는 메소드
     *
     * @param token
     */
    @Transactional
    public void deleteByAccessToken(String token) {
        tokenRepository.findByAccessToken(token).ifPresent(tokenRepository::delete);
    }

    /**
     * refresh token으로 Token 데이터를 가져와 access token 값을 업데이트하는 메소드
     *
     * @param refreshToken
     * @param accessToken
     */
    @Transactional
    public void updateByRefreshToken(String refreshToken, String accessToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException(CommonExceptionCode.JWT_UNKNOWN_ERROR.getData()))
                .convertToDomain();

        token.setAccessToken(accessToken);
        tokenRepository.save(token.convertToEntity());
    }
}