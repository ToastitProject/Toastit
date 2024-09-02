package alcoholboot.toastit.feature.basecocktail.repository;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.basecocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.basecocktail.repository.custom.CustomCocktailRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CocktailRepository extends MongoRepository<CocktailDocument, ObjectId>, CustomCocktailRepository {
//    Page<CocktailDocument> findByStrGlass(String glass, Pageable pageable);
//    Page<CocktailDocument> findByStrCategory(String category, Pageable pageable);

    @Query("{ $or: [ " +
            "{ 'strIngredient1': ?0 }, " +
            "{ 'strIngredient2': ?0 }, " +
            "{ 'strIngredient3': ?0 }, " +
            "{ 'strIngredient4': ?0 }, " +
            "{ 'strIngredient5': ?0 }, " +
            "{ 'strIngredient6': ?0 }, " +
            "{ 'strIngredient7': ?0 }, " +
            "{ 'strIngredient8': ?0 }, " +
            "{ 'strIngredient9': ?0 }, " +
            "{ 'strIngredient10': ?0 }, " +
            "{ 'strIngredient11': ?0 } " +
            "] }")
    List<Cocktail> findByAnyIngredient(String ingredient);
}