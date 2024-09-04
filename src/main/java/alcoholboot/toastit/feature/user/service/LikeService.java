package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.basecocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;

import org.bson.types.ObjectId;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LikeService {
    LikeEntity findByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId);

    void saveLike(LikeEntity likeEntity);

    void deleteLike(LikeEntity likeEntity);

    LikeEntity findByUserIdAndBasecocktailsId(Long userId, ObjectId objectId);

    int countByBasecocktailsId(ObjectId objectId);

    List<CraftCocktailEntity> findCraftCocktailsByUserId(Long userId);

    List<LikeEntity> findLikeEntityByUserId(Long userId);

}