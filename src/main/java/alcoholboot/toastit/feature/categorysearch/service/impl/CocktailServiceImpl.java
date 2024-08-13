package alcoholboot.toastit.feature.categorysearch.service.impl;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.entity.CocktailDocument;
import alcoholboot.toastit.feature.categorysearch.repository.CocktailRepository;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
    public List<Cocktail> getAllCocktails() {
        return cocktailRepository.findAll()
                .stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }

    // 레시피 보기 - 페이지
    @Override
    public Page<Cocktail> getAllCocktailsPaged(Pageable pageable) {
        return cocktailRepository.findAll(pageable)
                .map(CocktailDocument::convertToDomain);
    }

    // 카테고리 적용 - 재료
    @Override
    public List<Cocktail> getCocktailsByIngredient(String ingredient) {
        return cocktailRepository.findCocktailsByIngredient(ingredient)
                .stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }

    // 카테고리 적용 - 용기
    @Override
    public List<Cocktail> getCocktailsByGlass(String glass) {
        return cocktailRepository.findByStrGlass(glass)
                .stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }

    // 카테고리 적용 - 타입
    @Override
    public List<Cocktail> getCocktailsByType(String type) {
        return cocktailRepository.findByStrCategory(type).stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }

    // 카테고리 적용 - 복합
    @Override
    public List<Cocktail> getCocktailsByFilter(String ingredient, String glass, String type) {
        return cocktailRepository.findByIngredientAndGlassAndCategory(ingredient, glass, type)
                .stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }

    // id를 통해 얻어옴. 예외 발생할 수 있음
    @Override
    public Optional<Cocktail> getCocktailById(ObjectId id) {
        return cocktailRepository.findById(id)
                .map(CocktailDocument::convertToDomain);
    }
}