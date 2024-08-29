package alcoholboot.toastit.feature.defaultcocktail.service.impl;

import alcoholboot.toastit.feature.defaultcocktail.domain.Cocktail;
import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.defaultcocktail.repository.CocktailRepository;
import alcoholboot.toastit.feature.defaultcocktail.service.CocktailService;
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
public class    CocktailServiceImpl implements CocktailService {

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
    public Page<Cocktail> getCocktailsByIngredientPaged(String ingredient, Pageable pageable) {
        return cocktailRepository.findCocktailsByIngredientPage(ingredient, pageable)
                .map(CocktailDocument::convertToDomain);
    }

    // 카테고리 적용 - 용기
    @Override
    public Page<Cocktail> getCocktailsByGlassPaged(String glass, Pageable pageable) {
        return cocktailRepository.findByStrGlass(glass, pageable)
                .map(CocktailDocument::convertToDomain);
    }

    // 카테고리 적용 - 타입
    @Override
    public Page<Cocktail> getCocktailsByTypePaged(String type, Pageable pageable) {
        return cocktailRepository.findByStrCategory(type, pageable)
                .map(CocktailDocument::convertToDomain);
    }

    @Override
    public Page<Cocktail> getCocktailsByIngredientAndGlassPaged(String ingredient, String glass, Pageable pageable) {
        return cocktailRepository.findByIngredientAndGlass(ingredient, glass, pageable)
                .map(CocktailDocument::convertToDomain);
    }

    @Override
    public Page<Cocktail> getCocktailsByIngredientAndTypePaged(String ingredient, String type, Pageable pageable) {
        return cocktailRepository.findByIngredientAndCategoryPage(ingredient, type, pageable)
                .map(CocktailDocument::convertToDomain);
    }

    @Override
    public Page<Cocktail> getCocktailsByGlassAndTypePaged(String glass, String type, Pageable pageable) {
        return cocktailRepository.findByGlassAndCategoryPage(glass, type, pageable)
                .map(CocktailDocument::convertToDomain);
    }

    // 카테고리 적용 - 복합
    @Override
    public Page<Cocktail> getCocktailsByFilterPaged(String ingredient, String glass, String type, Pageable pageable) {
        return cocktailRepository.findByIngredientAndGlassAndCategoryPage(ingredient, glass, type, pageable)
                .map(CocktailDocument::convertToDomain);
    }

    // id를 통해 얻어옴. 예외 발생할 수 있음
    @Override
    public Optional<Cocktail> getCocktailById(ObjectId id) {
        return cocktailRepository.findById(id)
                .map(CocktailDocument::convertToDomain);
    }

    // 랜덤한 칵테일을 반환함
    @Override
    public List<Cocktail> getRandomCocktails(int count) {
        return cocktailRepository.findRandomCocktails(count)
                .stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cocktail> getCocktailsByIngredient(String ingredient) {
        return cocktailRepository.findByAnyIngredient(ingredient);
    }
}