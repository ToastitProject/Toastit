package alcoholboot.toastit.feature.user.service.impl;

//import alcoholboot.toastit.feature.user.domain.Like;
//import alcoholboot.toastit.feature.user.entity.LikeEntity;
//import alcoholboot.toastit.feature.user.repository.LikeRepository;
//import alcoholboot.toastit.feature.user.service.LikeService;
//import lombok.RequiredArgsConstructor;
//import org.bson.types.ObjectId;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;

//@Service
//@RequiredArgsConstructor
//public class LikeServiceImpl implements LikeService {
//    private final LikeRepository likeRepository;
//
//    public void save(Like like) {
//        LikeEntity entity = like.convertToEntity();
//        likeRepository.save(entity);
//    }
//
//    public void delete(Like like) {
//        LikeEntity entity = like.convertToEntity();
//        likeRepository.delete(entity);
//    }
//
//    @Override
//    public Optional<LikeEntity> findByUserIdAndCocktailId(Long userId, ObjectId cocktailId) {
//        return likeRepository.findByUserIdAndCocktailId(userId, cocktailId);
//    }
//}
