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
    //의존성 주입
    private final LikeRepository likerepository;

    /**
     * DB에 좋아요 객체를 저장합니다
     * @param likeEntity : 저장 할 좋아요 객체 입니다.
     */
    public void saveLike (LikeEntity likeEntity) {
        likerepository.save(likeEntity);
    }

    /**
     * 사용자의 ID 와 커스텀 칵테일 레시피 ID 로 좋아요 객체를 찾습니다.
     * @param userId : 좋아요를 누른 사용자 ID 입니다.
     * @param craftCocktailId : 좋아요 객체에 저장된 커스텀 칵테일 레시피 ID 입니다.
     * @return : 사용자가 커스텀 칵테일 레시피에 좋아요를 했다면 좋아요 객체를 반환합니다.
     */
    @Override
    public LikeEntity findByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId) {
        return likerepository.findByUserIdAndCraftCocktailId(userId,craftCocktailId);
    }

    /**
     * 좋아요 객체를 DB 에서 삭제합니다
     * @param likeEntity : 삭제할 좋아요 객체 입니다.
     */
    @Transactional
    public void deleteLike(LikeEntity likeEntity){
        likerepository.delete(likeEntity);
    }

    /**
     * 사용자의 ID 와 기본 칵테일 ID 로 좋아요 객체를 찾습니다.
     * @param userId : 좋아요를 누른 사용자 ID 입니다.
     * @param objectId : 좋아요 객체에 저장된 기본 칵테일 ID 입니다.
     * @return : 사용자가 기본 칵테일에 좋아요를 했다면 좋아요 객체를 반환합니다.
     */
    @Override
    public LikeEntity findByUserIdAndBasecocktailsId(Long userId, ObjectId objectId) {
        return likerepository.findByUserIdAndBasecocktailsId(userId,objectId);
    }

    /**
     * 기본 칵테일의 좋아요 갯수를 찾습니다.
     * @param objectId : 좋아요의 갯수를 찾을 기본 칵테일 ID 입니다.
     * @return : 총 받은 좋아요 갯수를 반환합니다.
     */
    @Override
    public int countByBasecocktailsId(ObjectId objectId) {
        return likerepository.countByBasecocktailsId(objectId);
    }

    /**
     * 커스텀 칵테일 레시피들 중 특정 사용자가 좋아요 한 레시피들을 찾습니다.
     * @param userId : 사용자의 아이디 입니다.
     * @return : 사용자가 좋아요를 한 커스텀 칵테일 레시피들을 List 자료구조로 반환합니다.
     */
    @Override
    public List<CraftCocktailEntity> findCraftCocktailsByUserId(Long userId) {
        return likerepository.findCraftCocktailsByUserId(userId);
    }

    /**
     * 사용자의 아이디로 좋아요 객체를 찾습니다.
     * @param userId : 좋아요 객체를 찾을 사용자의 아이디 입니다.
     * @return : 해당 사용자의 좋아요 객체를 List 자료구조로 반환합니다.
     */
    @Override
    public List<LikeEntity> findLikeEntityByUserId(Long userId) {
        return likerepository.findLikeEntityByUserId(userId);
    }


}
