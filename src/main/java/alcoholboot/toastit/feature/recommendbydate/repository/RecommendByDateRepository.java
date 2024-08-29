package alcoholboot.toastit.feature.recommendbydate.repository;

import alcoholboot.toastit.feature.recommendbydate.entity.RecommendByDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendByDateRepository extends JpaRepository<RecommendByDate, Long> {

}
