package alcoholboot.toastit.auth.jwt.repository;

import alcoholboot.toastit.auth.jwt.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenCrudRepository extends CrudRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByUserEntityId(Long userEntityId);
    Optional<TokenEntity> findByAccessToken(String accessToken);
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}
