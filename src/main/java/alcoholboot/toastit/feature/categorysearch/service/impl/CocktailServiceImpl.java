package alcoholboot.toastit.feature.categorysearch.service.impl;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.repository.CocktailRepository;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CocktailServiceImpl implements CocktailService {

    private final CocktailRepository cocktailRepository;

    // 모든 레시피 보기
    @Override
    public Page<Cocktail> getAllCocktails(Pageable pageable) {
        return cocktailRepository.findAll(pageable);
    }

    // 카테고리 적용 - 재료
    @Override
    public Page<Cocktail> getCocktailsByIngredient(String ingredient, Pageable pageable) {
        return cocktailRepository.findByStrIngredientsContaining(ingredient, pageable);
    }

    // 카테고리 적용 - 용기
    @Override
    public Page<Cocktail> getCocktailsByGlass(String glass, Pageable pageable) {
        return cocktailRepository.findByStrGlass(glass, pageable);
    }

    // 카테고리 적용 - 카테고리(strCategory)
    @Override
    public Page<Cocktail> getCocktailsByCategory(String category, Pageable pageable) {
        return cocktailRepository.findByStrCategory(category, pageable);
    }

    // 카테고리 적용 - 복합
    @Override
    public Page<Cocktail> getCocktailsByMultipleFilters(String ingredient, String glass, String category, Pageable pageable) {
        return cocktailRepository.findByStrIngredientsContainingAndStrGlassAndStrCategory(ingredient, glass, category, pageable);
    }

    // id를 통해 얻어옴. 예외 발생
    // 여산님이 만드신 공통 예외나
    // 효승님이 만들어주신 예외로 처리하도록 변경 필요
    // 수정 예정
    @Override
    public Cocktail getCocktailById(String id) {
        return cocktailRepository.findById(id).orElseThrow(() -> new RuntimeException("Cocktail not found"));
    }
}