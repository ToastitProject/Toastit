package alcoholboot.toastit.feature.categorysearch.service;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CocktailService {
    // 카테고리 검색
    Page<Cocktail> getAllCocktails(Pageable pageable);
    Page<Cocktail> getCocktailsByIngredient(String ingredient, Pageable pageable);
    Page<Cocktail> getCocktailsByGlass(String glass, Pageable pageable);
    Page<Cocktail> getCocktailsByCategory(String category, Pageable pageable);
    Page<Cocktail> getCocktailsByMultipleFilters(String ingredient, String glass, String category, Pageable pageable);
    Cocktail getCocktailById(String id);
}