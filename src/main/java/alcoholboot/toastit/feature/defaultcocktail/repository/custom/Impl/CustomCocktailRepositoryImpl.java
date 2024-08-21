package alcoholboot.toastit.feature.defaultcocktail.repository.custom.Impl;

import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.defaultcocktail.repository.custom.CustomCocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class CustomCocktailRepositoryImpl implements CustomCocktailRepository {
    private final MongoTemplate mongoTemplate;

//    @Override
//    public List<CocktailDocument> findCocktailsByIngredient(String ingredient) {
//        // Criteria 생성
//        Criteria criteria = new Criteria().orOperator(
//                Criteria.where("strIngredient1").regex(ingredient, "i"),
//                Criteria.where("strIngredient2").regex(ingredient, "i"),
//                Criteria.where("strIngredient3").regex(ingredient, "i"),
//                Criteria.where("strIngredient4").regex(ingredient, "i"),
//                Criteria.where("strIngredient5").regex(ingredient, "i"),
//                Criteria.where("strIngredient6").regex(ingredient, "i")
//        );
//
//        // 쿼리 생성
//        Query query = new Query(criteria);
//
//        // 쿼리 실행
//        return mongoTemplate.find(query, CocktailDocument.class);
//    }
//
//    @Override
//    public List<CocktailDocument> findByIngredientAndGlassAndCategory(String ingredient, String glass, String category) {
//        // Criteria 생성
//        Criteria ingredientCriteria = new Criteria().orOperator(
//                Criteria.where("strIngredient1").regex(ingredient, "i"),
//                Criteria.where("strIngredient2").regex(ingredient, "i"),
//                Criteria.where("strIngredient3").regex(ingredient, "i"),
//                Criteria.where("strIngredient4").regex(ingredient, "i"),
//                Criteria.where("strIngredient5").regex(ingredient, "i"),
//                Criteria.where("strIngredient6").regex(ingredient, "i")
//        );
//
//        Criteria glassAndCategoryCriteria = new Criteria().andOperator(
//                Criteria.where("strGlass").is(glass),
//                Criteria.where("strCategory").is(category)
//        );
//
//        Criteria combinedCriteria = new Criteria().andOperator(
//                ingredientCriteria,
//                glassAndCategoryCriteria
//        );
//
//        Query query = new Query(combinedCriteria);
//
//        return mongoTemplate.find(query, CocktailDocument.class);
//    }

    @Override
    public Page<CocktailDocument> findCocktailsByIngredientPage(String ingredient, Pageable pageable) {
//      Criteria 생성
        Criteria criteria = new Criteria().orOperator(
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

        // 쿼리 생성
        Query query = new Query(criteria);

        // 쿼리 실행
        List<CocktailDocument> cocktails = mongoTemplate.find(query, CocktailDocument.class);

        // 쿼리 조건에 맞는 전체 문서의 수를 계산
        long total = mongoTemplate.count(query.skip(-1).limit(-1), CocktailDocument.class);

        return new PageImpl<>(cocktails, pageable, total);
    }

    @Override
    public Page<CocktailDocument> findByIngredientAndGlassAndCategoryPage(String ingredient, String glass, String category, Pageable pageable) {
//      Criteria 생성
        Criteria criteria = new Criteria().orOperator(
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

        Criteria glassAndCategoryCriteria = new Criteria().andOperator(
                Criteria.where("strGlass").is(glass),
                Criteria.where("strCategory").is(category)
        );

        Criteria combinedCriteria = new Criteria().andOperator(
                criteria,
                glassAndCategoryCriteria
        );

        // 쿼리 생성
        Query query = new Query(combinedCriteria);

        // 쿼리 실행
        List<CocktailDocument> cocktails = mongoTemplate.find(query, CocktailDocument.class);

        // 쿼리 조건에 맞는 전체 문서의 수를 계산
        long total = mongoTemplate.count(query.skip(-1).limit(-1), CocktailDocument.class);

        return new PageImpl<>(cocktails, pageable, total);
    }
}