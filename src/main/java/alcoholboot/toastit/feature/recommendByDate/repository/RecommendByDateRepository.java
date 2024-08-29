package alcoholboot.toastit.feature.recommendByDate.repository;

import alcoholboot.toastit.feature.recommendByDate.entity.RecommendByDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendByDateRepository extends JpaRepository<RecommendByDate, Long> {

}
