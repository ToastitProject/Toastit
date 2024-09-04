package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.basecocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;

import org.bson.types.ObjectId;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    LikeEntity findByUserIdAndCraftCocktailId(Long userId, Long craftCocktailId);

    LikeEntity findByUserIdAndBasecocktailsId(Long userId, ObjectId objectId);

    @Query("SELECT COUNT(l) FROM LikeEntity l WHERE l.basecocktailsId = :basecocktailId")
    int countByBasecocktailsId(@Param("basecocktailId") ObjectId basecocktailId);

    @Query("SELECT l.craftCocktail FROM LikeEntity l WHERE l.user.id = :userId")
    List<CraftCocktailEntity> findCraftCocktailsByUserId(@Param("userId") Long userId);

    List<LikeEntity> findLikeEntityByUserId(Long userId);


}