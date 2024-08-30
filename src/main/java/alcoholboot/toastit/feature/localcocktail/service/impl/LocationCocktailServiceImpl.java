package alcoholboot.toastit.feature.localcocktail.service.impl;

import alcoholboot.toastit.feature.defaultcocktail.domain.Cocktail;
import alcoholboot.toastit.feature.defaultcocktail.service.CocktailService;
import alcoholboot.toastit.feature.localcocktail.repository.LocationCocktailRepository;
import alcoholboot.toastit.feature.localcocktail.service.LocationCocktailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * {@link LocationCocktailService} LocationCocktailService 인터페이스의 구현체
 * @see LocationCocktailRepository
 * @see CocktailService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LocationCocktailServiceImpl implements LocationCocktailService {

    private final LocationCocktailRepository locationCocktailRepository;
    private final CocktailService cocktailService;

    /**
     * 시와 도에 따른 칵테일 추천
     *
     * @param city 시 정보
     * @param province 도 정보
     * @return 추천 칵테일 또는 null
     */
    @Override
    public Cocktail getCocktailForLocation(String city, String province) {
        String ingredientsData = getIngredients(city, province);

        if (ingredientsData == null || ingredientsData.isEmpty()) {
            return null;
        }

        List<String> ingredients = Arrays.asList(ingredientsData.split(","));
        return getRandomCocktailByIngredients(ingredients);
    }

    /**
     * 시와 도에 따른 재료 조회
     *
     * @param si 시 정보
     * @param deo 도 정보
     * @return 재료 목록 문자열
     */
    @Override
    public String getIngredients(String si, String deo) {
        return locationCocktailRepository.findIngredientsBySiAndDeo(si, deo);
    }

    /**
     * 재료 목록에 따른 무작위 칵테일 추천
     *
     * @param ingredients 재료 목록
     * @return 추천 칵테일
     */
    @Override
    public Cocktail getRandomCocktailByIngredients(List<String> ingredients) {
        Random random = new Random();

        while (true) {
            String randomIngredient = ingredients.get(random.nextInt(ingredients.size()));
            log.debug("랜덤 선택 재료: {}", randomIngredient);

            List<Cocktail> cocktails = cocktailService.getCocktailsByIngredient(randomIngredient);
            if (!cocktails.isEmpty()) {
                return cocktails.get(random.nextInt(cocktails.size()));
            }
        }
    }
}