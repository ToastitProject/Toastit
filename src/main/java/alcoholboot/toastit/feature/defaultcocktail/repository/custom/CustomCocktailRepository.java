package alcoholboot.toastit.feature.defaultcocktail.repository.custom;

import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCocktailRepository {
    // 각 기능에 페이징 기능을 추가하기 위해서 추가
    Page<CocktailDocument> findCocktailsByIngredientPage(String ingredient, Pageable pageable);
    Page<CocktailDocument> findCocktailsByGlassPage(String glass, Pageable pageable);
    Page<CocktailDocument> findCocktailsByCategoryPage(String category, Pageable pageable);
    Page<CocktailDocument> findByIngredientAndGlass(String ingredient, String glass, Pageable pageable);
    Page<CocktailDocument> findByIngredientAndCategoryPage(String ingredient, String category, Pageable pageable);
    Page<CocktailDocument> findByGlassAndCategoryPage(String glass, String category, Pageable pageable);
    Page<CocktailDocument> findByIngredientAndGlassAndCategoryPage(String ingredient, String glass, String category, Pageable pageable);

    // 랜덤 레시피를 반환
    List<CocktailDocument> findRandomCocktails(int count);
}
