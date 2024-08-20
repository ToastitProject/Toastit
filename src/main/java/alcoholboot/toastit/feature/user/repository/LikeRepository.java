package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.user.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    LikeEntity findByUserIdAndCustomCocktailId (Long userId, Long customCocktailId);
}

