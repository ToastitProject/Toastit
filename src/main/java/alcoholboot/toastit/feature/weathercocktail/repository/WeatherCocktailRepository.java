package alcoholboot.toastit.feature.weathercocktail.repository;

import alcoholboot.toastit.feature.weathercocktail.entity.WeatherCocktailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherCocktailRepository extends JpaRepository<WeatherCocktailEntity, Long> {
    WeatherCocktailEntity findByWeather(String weather);
}
