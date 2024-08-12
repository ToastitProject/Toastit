package alcoholboot.toastit.feature.categorysearch.service;

import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface CocktailService {
    // 카테고리 검색
    List<CocktailEntity> getAllCocktails();
    Page<CocktailEntity> getAllCocktailsPaged(Pageable pageable);
    List<CocktailEntity> getCocktailsByIngredient(String ingredient);
    List<CocktailEntity> getCocktailsByGlass(String glass);
    List<CocktailEntity> getCocktailsByType(String type);
    List<CocktailEntity> getCocktailsByFilter(String ingredient, String glass, String type);
    Optional<CocktailEntity> getCocktailById(String id);
}