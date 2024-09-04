package alcoholboot.toastit.feature.basecocktail.service.impl;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.basecocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.basecocktail.repository.CocktailRepository;
import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CocktailService 인터페이스의 구현 클래스입니다.
 * 이 클래스는 칵테일 정보를 조회하고 필터링하는 다양한 메소드를 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class    CocktailServiceImpl implements CocktailService {

    private final CocktailRepository cocktailRepository;


    /**
     * 페이징 처리된 모든 칵테일 레시피를 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 페이징 처리된 칵테일 목록
     */
    @Override
    public Page<Cocktail> getAllCocktailsPaged(Pageable pageable) {
        return cocktailRepository.findAll(pageable)
                .map(CocktailDocument::convertToDomain);
    }


    /**
     * 재료, 잔 종류, 타입을 모두 고려하여 칵테일을 페이징 처리하여 조회합니다.
     *
     * @param ingredient 검색할 재료 목록
     * @param glass 검색할 잔 종류
     * @param type 검색할 칵테일 타입
     * @param pageable 페이징 정보
     * @return 페이징 처리된 칵테일 목록
     */
    @Override
    public Page<Cocktail> getCocktailsByFilterPaged(List<String> ingredient, String glass, String type, Pageable pageable) {
        return cocktailRepository.findByIngredientAndGlassAndCategoryPage(ingredient, glass, type, pageable)
                .map(CocktailDocument::convertToDomain);
    }


    /**
     * 주어진 ID에 해당하는 칵테일을 조회합니다.
     *
     * @param id 조회할 칵테일의 ID
     * @return 찾은 칵테일 (Optional)
     */
    @Override
    public Optional<Cocktail> getCocktailById(ObjectId id) {
        return cocktailRepository.findById(id)
                .map(CocktailDocument::convertToDomain);
    }


    /**
     * 무작위로 선택된 칵테일을 반환합니다.
     *
     * @param count 반환할 칵테일의 수
     * @return 무작위로 선택된 칵테일 목록
     */
    @Override
    public List<Cocktail> getRandomCocktails(int count) {
        return cocktailRepository.findRandomCocktails(count)
                .stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }


    /**
     * 특정 재료를 포함하는 모든 칵테일을 조회합니다.
     *
     * @param ingredient 검색할 재료
     * @return 해당 재료를 포함하는 칵테일 목록
     */
    @Override
    public List<Cocktail> getCocktailsByIngredient(String ingredient) {
        return cocktailRepository.findByIngredient(ingredient)
                .stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }


    @Override
    public List<Cocktail> getCocktailsByName(List<String> names) {
        return cocktailRepository.findCocktailsByName(names)
                .stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Cocktail getSingleCocktailByName(String name) {
        return cocktailRepository.findSingleCocktailByName(name)
                .convertToDomain();
    }

    @Override
    public List<Cocktail> getCocktailsById(List<ObjectId> ids) {
        List<CocktailDocument> cocktailDocuments = cocktailRepository.findByIdIn(ids);
        return cocktailDocuments.stream()
                .map(CocktailDocument::convertToDomain)
                .collect(Collectors.toList());
    }
}