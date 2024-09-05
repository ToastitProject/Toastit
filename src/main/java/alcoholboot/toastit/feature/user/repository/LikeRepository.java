package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;

import org.bson.types.ObjectId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 좋아요와 관련된 데이터베이스 작업을 처리하는 리포지토리 인터페이스.
 * 사용자와 칵테일 간의 좋아요 정보 조회 및 좋아요 수 계산을 위한 쿼리 메서드를 제공합니다.
 */
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    /**
     * 특정 사용자가 특정 커스텀 칵테일에 대해 좋아요한 정보를 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @param craftCocktailId 커스텀 칵테일 ID
     * @return LikeEntity 객체 (좋아요 정보)
     */
    LikeEntity findByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId);

    /**
     * 특정 사용자가 특정 기본 칵테일에 대해 좋아요한 정보를 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @param objectId 기본 칵테일의 MongoDB ObjectId
     * @return LikeEntity 객체 (좋아요 정보)
     */
    LikeEntity findByUserIdAndBasecocktailsId(Long userId, ObjectId objectId);

    /**
     * 특정 기본 칵테일에 대한 좋아요 수를 계산하는 메서드.
     *
     * @param basecocktailId 기본 칵테일의 MongoDB ObjectId
     * @return 기본 칵테일에 대한 좋아요 수
     */
    @Query("SELECT COUNT(l) FROM LikeEntity l WHERE l.basecocktailsId = :basecocktailId")
    int countByBasecocktailsId(@Param("basecocktailId") ObjectId basecocktailId);

    /**
     * 특정 사용자가 좋아요한 커스텀 칵테일 목록을 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @return 사용자가 좋아요한 커스텀 칵테일 리스트
     */
    @Query("SELECT l.craftCocktail FROM LikeEntity l WHERE l.user.id = :userId")
    List<CraftCocktailEntity> findCraftCocktailsByUserId(@Param("userId") Long userId);

    /**
     * 특정 사용자가 좋아요한 모든 좋아요 정보를 조회하는 메서드.
     *
     * @param userId 사용자 ID
     * @return 사용자가 좋아요한 LikeEntity 리스트
     */
    List<LikeEntity> findLikeEntityByUserId(Long userId);
}