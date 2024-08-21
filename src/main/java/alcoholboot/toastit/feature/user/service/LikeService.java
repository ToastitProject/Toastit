package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.entity.LikeEntity;
import org.springframework.stereotype.Service;


@Service
public interface LikeService {
    LikeEntity findByUserIdAndCustomCocktailId (Long userId, Long CustomCocktailId);
void saveLike (LikeEntity likeEntity);
void deleteLike (LikeEntity likeEntity);
}
