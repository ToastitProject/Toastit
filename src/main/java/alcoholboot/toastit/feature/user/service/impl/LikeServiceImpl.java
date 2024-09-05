package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.repository.LikeRepository;
import alcoholboot.toastit.feature.user.service.LikeService;

import lombok.RequiredArgsConstructor;

import org.bson.types.ObjectId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 좋아요 관련 비즈니스 로직을 처리하는 서비스 구현 클래스.
 * 사용자가 좋아요한 칵테일 정보를 관리하고, 좋아요 객체를 저장 및 삭제하는 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    // 좋아요 관련 데이터베이스 작업을 처리하는 리포지토리
    private final LikeRepository likerepository;

    /**
     * 새로운 좋아요 객체를 저장하는 메서드.
     *
     * @param likeEntity 저장할 좋아요 객체
     */
    public void saveLike(LikeEntity likeEntity) {
        likerepository.save(likeEntity);
    }

    /**
     * 특정 사용자가 특정 커스텀 칵테일 레시피에 좋아요한 정보를 조회하는 메서드.
     *
     * @param userId 좋아요를 누른 사용자 ID
     * @param craftCocktailId 좋아요 객체에 저장된 커스텀 칵테일 레시피 ID
     * @return 해당 사용자의 좋아요 객체
     */
    @Override
    public LikeEntity findByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId) {
        return likerepository.findByUserIdAndCraftCocktailId(userId, craftCocktailId);
    }

    /**
     * 좋아요 객체를 삭제하는 메서드.
     *
     * @param likeEntity 삭제할 좋아요 객체
     */
    @Transactional
    public void deleteLike(LikeEntity likeEntity) {
        likerepository.delete(likeEntity);
    }

    /**
     * 특정 사용자가 특정 기본 칵테일에 좋아요한 정보를 조회하는 메서드.
     *
     * @param userId 좋아요를 누른 사용자 ID
     * @param objectId 기본 칵테일의 MongoDB ObjectId
     * @return 해당 사용자의 좋아요 객체
     */
    @Override
    public LikeEntity findByUserIdAndBasecocktailsId(Long userId, ObjectId objectId) {
        return likerepository.findByUserIdAndBasecocktailsId(userId, objectId);
    }

    /**
     * 특정 기본 칵테일이 받은 좋아요 수를 조회하는 메서드.
     *
     * @param objectId 좋아요 수를 조회할 기본 칵테일 ID
     * @return 해당 기본 칵테일이 받은 좋아요 수
     */
    @Override
    public int countByBasecocktailsId(ObjectId objectId) {
        return likerepository.countByBasecocktailsId(objectId);
    }

    /**
     * 특정 사용자가 좋아요한 커스텀 칵테일 레시피 목록을 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @return 사용자가 좋아요한 커스텀 칵테일 레시피 목록
     */
    @Override
    public List<CraftCocktailEntity> findCraftCocktailsByUserId(Long userId) {
        return likerepository.findCraftCocktailsByUserId(userId);
    }

    /**
     * 특정 사용자가 좋아요한 모든 좋아요 객체를 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 좋아요 객체 목록
     */
    @Override
    public List<LikeEntity> findLikeEntityByUserId(Long userId) {
        return likerepository.findLikeEntityByUserId(userId);
    }
}