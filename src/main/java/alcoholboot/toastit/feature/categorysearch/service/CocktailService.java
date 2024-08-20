package alcoholboot.toastit.feature.categorysearch.service;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CocktailService {
    // 카테고리 검색
    List<Cocktail> getAllCocktails();
    Page<Cocktail> getAllCocktailsPaged(Pageable pageable);
    List<Cocktail> getCocktailsByIngredient(String ingredient);
    List<Cocktail> getCocktailsByGlass(String glass);
    List<Cocktail> getCocktailsByType(String type);
    List<Cocktail> getCocktailsByFilter(String ingredient, String glass, String type);
    Optional<Cocktail> getCocktailById(ObjectId id);
}