package alcoholboot.toastit.feature.weathercocktail.repository;

import alcoholboot.toastit.feature.weathercocktail.entity.WeatherCocktailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WeatherCocktailRepository extends JpaRepository<WeatherCocktailEntity, Long> {
    /**
     * 날씨에 맞는 칵테일을 재료들을 분류해놓은 테이블인 weather_cocktails에서
     * 날씨를 통해서 재료들을 가져옴
     */
    WeatherCocktailEntity findByWeather(String weather);
}
