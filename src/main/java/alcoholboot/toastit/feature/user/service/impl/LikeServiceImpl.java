package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.repository.LikeRepository;
import alcoholboot.toastit.feature.user.service.LikeService;

import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likerepository;

    @Override
    public LikeEntity getByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId) {
        return likerepository.findByUserIdAndCraftCocktailId(userId, craftCocktailId);
    }

    @Transactional
    public void saveLike (LikeEntity likeEntity) {
        likerepository.save(likeEntity);
    }

    @Transactional
    public void deleteLike(LikeEntity likeEntity){
        likerepository.delete(likeEntity);
    }

    @Override
    public LikeEntity findByUserIdAndDefaultCocktailsId(Long userId, ObjectId objectId) {
        return likerepository.findByUserIdAndDefaultCocktailsId(userId,objectId);
    }

    @Override
    public int countByDefaultCocktailsId(ObjectId objectId) {
        return likerepository.countByDefaultCocktailsId(objectId);
    }
}