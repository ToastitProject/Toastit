package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.domain.Like;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import org.bson.types.ObjectId;

import java.util.Optional;

public interface LikeService {
    void save(Like like);
    void delete(Like like);
    Optional<LikeEntity> findByUserIdAndCocktailId(Long userId, ObjectId cocktailId);
}
