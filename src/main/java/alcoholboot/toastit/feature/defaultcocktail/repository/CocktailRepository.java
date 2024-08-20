package alcoholboot.toastit.feature.defaultcocktail.repository;

import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.defaultcocktail.repository.custom.CustomCocktailRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CocktailRepository extends MongoRepository<CocktailDocument, ObjectId>, CustomCocktailRepository {
    List<CocktailDocument> findByStrGlass(String glass);
    List<CocktailDocument> findByStrCategory(String category);
}