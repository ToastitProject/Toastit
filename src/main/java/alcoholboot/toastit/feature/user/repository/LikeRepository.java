package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.user.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
}
