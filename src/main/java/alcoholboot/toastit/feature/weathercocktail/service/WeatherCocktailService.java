package alcoholboot.toastit.feature.weathercocktail.service;

import alcoholboot.toastit.feature.weathercocktail.entity.WeatherCocktailEntity;
import alcoholboot.toastit.feature.weathercocktail.repository.WeatherCocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherCocktailService {
    private final WeatherCocktailRepository weatherCocktailRepository;

    /**
     * 미리 분류해둔 테이블인 weather_cocktails에서 날씨를 통해서 재료들을 찾는다.
     *
     * @param weather
     * @return
     */

    public List<String> getIngredientsByWeather(String weather) {
        WeatherCocktailEntity entity = weatherCocktailRepository.findByWeather(weather);
        List<String> ingredients = new ArrayList<>();

        ingredients.add(entity.getIngredient1());
        ingredients.add(entity.getIngredient2());
        ingredients.add(entity.getIngredient3());
        ingredients.add(entity.getIngredient4());
        ingredients.add(entity.getIngredient5());
        ingredients.add(entity.getIngredient6());
        ingredients.add(entity.getIngredient7());
        ingredients.add(entity.getIngredient8());
        ingredients.add(entity.getIngredient9());

        return ingredients;
    }
}
