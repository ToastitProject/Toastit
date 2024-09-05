package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;

import org.bson.types.ObjectId;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 좋아요 기능을 위한 서비스 인터페이스.
 * 사용자가 좋아요한 칵테일 정보를 조회하고, 좋아요를 저장하거나 삭제하는 메서드를 제공합니다.
 */
@Service
public interface LikeService {

    /**
     * 특정 사용자와 커스텀 칵테일 레시피 간의 좋아요 관계를 조회하는 메서드.
     *
     * @param userId         사용자 ID
     * @param craftCocktailId 커스텀 칵테일 레시피 ID
     * @return 좋아요 엔티티 객체
     */
    LikeEntity findByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId);

    /**
     * 새로운 좋아요 관계를 저장하는 메서드.
     *
     * @param likeEntity 좋아요 엔티티 객체
     */
    void saveLike(LikeEntity likeEntity);

    /**
     * 기존 좋아요 관계를 삭제하는 메서드.
     *
     * @param likeEntity 삭제할 좋아요 엔티티 객체
     */
    void deleteLike(LikeEntity likeEntity);

    /**
     * 특정 사용자와 기본 칵테일 간의 좋아요 관계를 조회하는 메서드.
     *
     * @param userId   사용자 ID
     * @param objectId 기본 칵테일의 MongoDB ObjectId
     * @return 좋아요 엔티티 객체
     */
    LikeEntity findByUserIdAndBasecocktailsId(Long userId, ObjectId objectId);

    /**
     * 특정 기본 칵테일이 받은 좋아요 수를 조회하는 메서드.
     *
     * @param objectId 기본 칵테일의 MongoDB ObjectId
     * @return 좋아요 수
     */
    int countByBasecocktailsId(ObjectId objectId);

    /**
     * 특정 사용자가 좋아요한 커스텀 칵테일 목록을 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @return 좋아요한 커스텀 칵테일 리스트
     */
    List<CraftCocktailEntity> findCraftCocktailsByUserId(Long userId);

    /**
     * 특정 사용자가 좋아요한 모든 좋아요 엔티티 목록을 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @return 좋아요 엔티티 리스트
     */
    List<LikeEntity> findLikeEntityByUserId(Long userId);
}