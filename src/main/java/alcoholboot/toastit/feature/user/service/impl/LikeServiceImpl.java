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
    public LikeEntity findByUserIdAndCustomCocktailId(Long userId, Long customCocktailId) {
        return likerepository.findByUserIdAndCustomCocktailId(userId,customCocktailId);
    }

    public void saveLike (LikeEntity likeEntity) {
        likerepository.save(likeEntity);
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
}
