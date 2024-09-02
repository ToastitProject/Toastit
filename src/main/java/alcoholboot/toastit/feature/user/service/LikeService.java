package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.entity.LikeEntity;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public interface LikeService {

    LikeEntity getByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId);

    void saveLike(LikeEntity likeEntity);

    void deleteLike(LikeEntity likeEntity);

    LikeEntity findByUserIdAndDefaultCocktailsId(Long userId, ObjectId objectId);

    int countByDefaultCocktailsId(ObjectId objectId);
}