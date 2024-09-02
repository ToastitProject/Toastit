package alcoholboot.toastit.feature.climatecocktail.repository;

import alcoholboot.toastit.feature.climatecocktail.entity.RecommendByWeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendByWeatherRepository extends JpaRepository<RecommendByWeatherEntity, Long> {
    RecommendByWeatherEntity findByWeather(String weather);
}
