package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.user.entity.LikeEntity;
import org.bson.types.ObjectId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    Optional<LikeEntity> findByUserIdAndCocktailId(Long userId, ObjectId cocktailId);
}

