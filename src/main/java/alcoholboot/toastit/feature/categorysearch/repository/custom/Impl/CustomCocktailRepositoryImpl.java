package alcoholboot.toastit.feature.categorysearch.repository.custom.Impl;

import alcoholboot.toastit.feature.categorysearch.entity.CocktailDocument;
import alcoholboot.toastit.feature.categorysearch.repository.custom.CustomCocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@RequiredArgsConstructor
public class CustomCocktailRepositoryImpl implements CustomCocktailRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public List<CocktailDocument> findCocktailsByIngredient(String ingredient) {
        // Criteria 생성
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("strIngredient1").regex(ingredient, "i"),
                Criteria.where("strIngredient2").regex(ingredient, "i"),
                Criteria.where("strIngredient3").regex(ingredient, "i"),
                Criteria.where("strIngredient4").regex(ingredient, "i"),
                Criteria.where("strIngredient5").regex(ingredient, "i"),
                Criteria.where("strIngredient6").regex(ingredient, "i")
        );

        // 쿼리 생성
        Query query = new Query(criteria);

        // 쿼리 실행
        return mongoTemplate.find(query, CocktailDocument.class);
    }

    @Override
    public List<CocktailDocument> findByIngredientAndGlassAndCategory(String ingredient, String glass, String category) {
        // Criteria 생성
        Criteria ingredientCriteria = new Criteria().orOperator(
                Criteria.where("strIngredient1").regex(ingredient, "i"),
                Criteria.where("strIngredient2").regex(ingredient, "i"),
                Criteria.where("strIngredient3").regex(ingredient, "i"),
                Criteria.where("strIngredient4").regex(ingredient, "i"),
                Criteria.where("strIngredient5").regex(ingredient, "i"),
                Criteria.where("strIngredient6").regex(ingredient, "i")
        );

        Criteria glassAndCategoryCriteria = new Criteria().andOperator(
                Criteria.where("strGlass").is(glass),
                Criteria.where("strCategory").is(category)
        );

        Criteria combinedCriteria = new Criteria().andOperator(
                ingredientCriteria,
                glassAndCategoryCriteria
        );

        Query query = new Query(combinedCriteria);

        return mongoTemplate.find(query, CocktailDocument.class);
    }
}