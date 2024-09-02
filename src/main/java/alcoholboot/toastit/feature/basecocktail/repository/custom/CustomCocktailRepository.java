package alcoholboot.toastit.feature.basecocktail.repository.custom;

import alcoholboot.toastit.feature.basecocktail.entity.CocktailDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 사용자 정의 칵테일 검색 기능을 제공하는 리포지토리 인터페이스입니다.
 * 이 인터페이스는 다양한 조건으로 칵테일을 검색하고 페이징 처리된 결과를 반환하는 메소드들을 정의합니다.
 */
public interface CustomCocktailRepository {

    /**
     * 주어진 재료 목록, 잔 종류, 카테고리에 해당하는 칵테일을 검색하고 페이징 처리된 결과를 반환합니다.
     *
     * @param ingredient 검색할 재료 목록
     * @param glass 검색할 잔 종류
     * @param category 검색할 카테고리
     * @param pageable 페이징 정보
     * @return 검색 결과를 담은 Page 객체
     */
    Page<CocktailDocument> findByIngredientAndGlassAndCategoryPage(List<String> ingredient, String glass, String category, Pageable pageable);


    /**
     * 지정된 수만큼의 랜덤한 칵테일 레시피를 반환합니다.
     *
     * @param count 반환할 랜덤 칵테일의 수
     * @return 랜덤하게 선택된 칵테일 목록
     */
    List<CocktailDocument> findRandomCocktails(int count);


    /**
     * 주어진 이름 리스트에 해당하는 칵테일을 검색하고 결과를 반환합니다.
     *
     * @param name 대상이 될 이름
     * @return 검색 결과 칵테일 정보
     */
    List<CocktailDocument> findCocktailsByName(List<String> name);


    /**
     * 주어진 이름에 해당하는 칵테일을 검색하고 결과를 반환합니다.
     *
     * @param name 대상이 될 이름
     * @return 검색 결과 칵테일 정보
     */
    CocktailDocument findSingleCocktailByName(String name);


    /**
     * 주어진 재료에 해당하는 칵테일을 검색하고 결과를 반환합니다.
     *
     * @param ingredient 검색할 재료 이름
     * @return 검색 결과 칵테일 정보
     */
    List<CocktailDocument> findByIngredient(String ingredient);
}