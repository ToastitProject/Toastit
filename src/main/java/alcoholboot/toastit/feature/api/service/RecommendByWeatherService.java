package alcoholboot.toastit.feature.api.service;

import alcoholboot.toastit.feature.api.entity.RecommendByWeatherEntity;
import alcoholboot.toastit.feature.api.repository.RecommendByWeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendByWeatherService {
    private final RecommendByWeatherRepository recommendByWeatherRepository;

    public List<String> getIngredientsByWeather(String weather) {
        RecommendByWeatherEntity entity = recommendByWeatherRepository.findByWeather(weather);
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
