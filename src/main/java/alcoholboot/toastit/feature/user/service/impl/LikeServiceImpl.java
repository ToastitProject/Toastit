package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.feature.basecocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.repository.LikeRepository;
import alcoholboot.toastit.feature.user.service.LikeService;

import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likerepository;

    public void saveLike (LikeEntity likeEntity) {
        likerepository.save(likeEntity);
    }

    @Override
    public LikeEntity findByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId) {
        return likerepository.findByUserIdAndCraftCocktailId(userId,craftCocktailId);
    }

    @Transactional
    public void deleteLike(LikeEntity likeEntity){
        likerepository.delete(likeEntity);
    }

    @Override
    public LikeEntity findByUserIdAndBasecocktailsId(Long userId, ObjectId objectId) {
        return likerepository.findByUserIdAndBasecocktailsId(userId,objectId);
    }

    @Override
    public int countByBasecocktailsId(ObjectId objectId) {
        return likerepository.countByBasecocktailsId(objectId);
    }

    @Override
    public List<CraftCocktailEntity> findCraftCocktailsByUserId(Long userId) {
        return likerepository.findCraftCocktailsByUserId(userId);
    }

    @Override
    public List<LikeEntity> findLikeEntityByUserId(Long userId) {
        return likerepository.findLikeEntityByUserId(userId);
    }


}
