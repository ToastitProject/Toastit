package alcoholboot.toastit.feature.defaultcocktail.repository.custom.Impl;

import alcoholboot.toastit.feature.customcocktail.domain.Ingredient;
import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.defaultcocktail.repository.custom.CustomCocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.repository.Aggregation;

import java.util.List;

@RequiredArgsConstructor
public class CustomCocktailRepositoryImpl implements CustomCocktailRepository {
    private final MongoTemplate mongoTemplate;

    /**
     * 재료 검색을 위한 Criteria를 생성합니다.
     * 이 메서드는 strIngredient1부터 strIngredient11까지의 필드에 대해 검색 조건을 생성합니다.
     *
     * @param ingredient 검색할 재료 이름
     * @return 생성된 Criteria 객체
     */
    private Criteria createIngredientCriteria(String ingredient) {
        if (ingredient == null || ingredient.trim().isEmpty()) {
            return new Criteria();
        }
        return new Criteria().orOperator(
                Criteria.where("strIngredient1").regex(ingredient, "i"),
                Criteria.where("strIngredient2").regex(ingredient, "i"),
                Criteria.where("strIngredient3").regex(ingredient, "i"),
                Criteria.where("strIngredient4").regex(ingredient, "i"),
                Criteria.where("strIngredient5").regex(ingredient, "i"),
                Criteria.where("strIngredient6").regex(ingredient, "i"),
                Criteria.where("strIngredient7").regex(ingredient, "i"),
                Criteria.where("strIngredient8").regex(ingredient, "i"),
                Criteria.where("strIngredient9").regex(ingredient, "i"),
                Criteria.where("strIngredient10").regex(ingredient, "i"),
                Criteria.where("strIngredient11").regex(ingredient, "i")
        );
    }

    /**
     * 잔 종류 검색을 위한 Criteria를 생성합니다.
     *
     * @param glass 검색할 잔 종류
     * @return 생성된 Criteria 객체
     */
    private Criteria createGlassCriteria(String glass) {
        if (glass == null || glass.trim().isEmpty()) {
            return new Criteria();
        }
        return new Criteria().orOperator(
                Criteria.where("strGlass").regex(glass, "i")
        );
    }

    /**
     * 카테고리 검색을 위한 Criteria를 생성합니다.
     *
     * @param category 검색할 카테고리
     * @return 생성된 Criteria 객체
     */
    private Criteria createCategoryCriteria(String category) {
        if (category == null || category.trim().isEmpty()) {
            return new Criteria();
        }
        return new Criteria().orOperator(
                Criteria.where("strCategory").regex(category, "i")
        );
    }

    /**
     * 주어진 Criteria와 Pageable 객체를 사용하여 칵테일을 검색하고 Page 객체로 반환합니다.
     * 이 메서드는 실제 데이터베이스 쿼리를 수행하고 결과를 페이징 처리합니다.
     *
     * @param criteria 검색 조건
     * @param pageable 페이징 정보
     * @return 검색 결과를 담은 Page 객체
     */
    private Page<CocktailDocument> findCocktails(Criteria criteria, Pageable pageable) {
        // 쿼리 생성
        Query query = new Query(criteria).with(pageable);

        // 쿼리 실행
        List<CocktailDocument> cocktails = mongoTemplate.find(query, CocktailDocument.class);

        // 쿼리 조건에 맞는 전체 문서의 수를 계산
        long total = mongoTemplate.count(query.skip(-1).limit(-1), CocktailDocument.class);

        return new PageImpl<>(cocktails, pageable, total);
    }

    /**
     * 주어진 재료로 칵테일을 검색합니다.
     */
    @Override
    public Page<CocktailDocument> findCocktailsByIngredientPage(String ingredient, Pageable pageable) {

        return findCocktails(createIngredientCriteria(ingredient), pageable);
    }

    /**
     * 주어진 잔 종류로 칵테일을 검색합니다.
     */
    @Override
    public Page<CocktailDocument> findCocktailsByGlassPage(String glass, Pageable pageable) {

        return findCocktails(createGlassCriteria(glass), pageable);
    }

    /**
     * 주어진 카테고리로 칵테일을 검색합니다.
     */
    @Override
    public Page<CocktailDocument> findCocktailsByCategoryPage(String category, Pageable pageable) {

        return findCocktails(createCategoryCriteria(category), pageable);
    }

    /**
     * 주어진 재료와 잔 종류로 칵테일을 검색합니다.
     */
    @Override
    public Page<CocktailDocument> findByIngredientAndGlass(String ingredient, String glass, Pageable pageable) {

        // Criteria 생성
        Criteria combinedCriteria = new Criteria().andOperator(
                createIngredientCriteria(ingredient),
                createGlassCriteria(glass)
        );

        return findCocktails(combinedCriteria, pageable);
    }

    /**
     * 주어진 재료와 카테고리로 칵테일을 검색합니다.
     */
    @Override
    public Page<CocktailDocument> findByIngredientAndCategoryPage(String ingredient, String category, Pageable pageable) {

        // Criteria 생성
        Criteria combinedCriteria = new Criteria().andOperator(
                createIngredientCriteria(ingredient),
                createCategoryCriteria(category)
        );

        return findCocktails(combinedCriteria, pageable);
    }

    /**
     * 주어진 잔 종류와 카테고리로 칵테일을 검색합니다.
     */
    @Override
    public Page<CocktailDocument> findByGlassAndCategoryPage(String glass, String category, Pageable pageable) {

        // Criteria 생성
        Criteria combinedCriteria = new Criteria().andOperator(
                createGlassCriteria(glass),
                createCategoryCriteria(category)
        );

        return findCocktails(combinedCriteria, pageable);
    }

    /**
     * 주어진 재료, 잔 종류, 카테고리로 칵테일을 검색합니다.
     */
    @Override
    public Page<CocktailDocument> findByIngredientAndGlassAndCategoryPage(String ingredient, String glass, String category, Pageable pageable) {

        // Criteria 생성
        Criteria combinedCriteria = new Criteria().andOperator(
                createIngredientCriteria(ingredient),
                createGlassCriteria(glass),
                createCategoryCriteria(category)
        );

        return findCocktails(combinedCriteria, pageable);
    }

    /**
     *  랜덤한 칵테일을 반환한다.
     * @param count 반환할 칵테일 수
     * @return 랜덤한 칵테일
     */
    @Override
    public List<CocktailDocument> findRandomCocktails(int count) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.sample(count)
        );
        AggregationResults<CocktailDocument> results = mongoTemplate.aggregate(
                aggregation, "cocktails", CocktailDocument.class
        );
        return results.getMappedResults();
    }
}