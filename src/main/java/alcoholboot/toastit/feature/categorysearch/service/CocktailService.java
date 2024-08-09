package alcoholboot.toastit.feature.categorysearch.service;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CocktailService {
    // 카테고리 검색
    Page<CocktailEntity> getAllCocktails(Pageable pageable);
    Page<CocktailEntity> getCocktailsByIngredient(String ingredient, Pageable pageable);
    Page<CocktailEntity> getCocktailsByGlass(String glass, Pageable pageable);
    Page<CocktailEntity> getCocktailsByCategory(String category, Pageable pageable);
    Page<CocktailEntity> getCocktailsByMultipleFilters(String ingredient, String glass, String category, Pageable pageable);
    CocktailEntity getCocktailById(String id);
}