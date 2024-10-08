package alcoholboot.toastit.feature.basecocktail.service;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CocktailService {
    // 카테고리 검색
    Page<Cocktail> getAllCocktailsPaged(Pageable pageable);
    Page<Cocktail> getCocktailsByFilterPaged(List<String> ingredient, String glass, String type, Pageable pageable);
    Optional<Cocktail> getCocktailById(ObjectId id);

    // 랜덤한 칵테일 반환
    List<Cocktail> getRandomCocktails(int count);

    // 단일 재료로 반환
    List<Cocktail> getCocktailsByIngredient(String ingredient);

    // 이름으로 칵테일 반환
    List<Cocktail> getCocktailsByName(List<String> names);

    // 이름으로 칵테일 반환
    Cocktail getSingleCocktailByName(String name);

    List<Cocktail> getCocktailsById(List<ObjectId> ids);

    // 칵테일 전체의 이름만을 반환
    List<String> getAllNames();
}