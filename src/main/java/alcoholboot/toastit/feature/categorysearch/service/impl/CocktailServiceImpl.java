package alcoholboot.toastit.feature.categorysearch.service.impl;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import alcoholboot.toastit.feature.categorysearch.repository.CocktailRepository;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CocktailServiceImpl implements CocktailService {

    private final CocktailRepository cocktailRepository;

    // 모든 레시피 보기
    @Override
    public List<CocktailEntity> getAllCocktails() {
        return cocktailRepository.findAll().stream()
                .map(Cocktail::convertToEntity)
                .collect(Collectors.toList());
    }

    // 레시피 보기 - 페이지
    @Override
    public Page<CocktailEntity> getAllCocktailsPaged(Pageable pageable) {
        return cocktailRepository.findAll(pageable)
                .map(Cocktail::convertToEntity);
    }

    // 카테고리 적용 - 재료
    @Override
    public List<CocktailEntity> getCocktailsByIngredient(String ingredient) {
        return cocktailRepository.findByStrIngredientsContaining(ingredient).stream()
                .map(Cocktail::convertToEntity)
                .collect(Collectors.toList());
    }

    // 카테고리 적용 - 용기
    @Override
    public List<CocktailEntity> getCocktailsByGlass(String glass) {
        return cocktailRepository.findByStrGlass(glass).stream()
                .map(Cocktail::convertToEntity)
                .collect(Collectors.toList());
    }

    // 카테고리 적용 - 타입
    @Override
    public List<CocktailEntity> getCocktailsByType(String type) {
        return cocktailRepository.findByStrCategory(type).stream()
                .map(Cocktail::convertToEntity)
                .collect(Collectors.toList());
    }


    // 카테고리 적용 - 복합
    @Override
    public List<CocktailEntity> getCocktailsByFilter(String ingredient, String glass, String type) {
        return cocktailRepository.findByStrIngredientsContainingAndStrGlassAndStrCategory(ingredient, glass, type).stream()
                .map(Cocktail::convertToEntity)
                .collect(Collectors.toList());
    }


    // id를 통해 얻어옴. 예외 발생할 수 있음
    @Override
    public Optional<CocktailEntity> getCocktailById(String id) {
        return cocktailRepository.findById(id)
                .map(Cocktail::convertToEntity);
    }
}