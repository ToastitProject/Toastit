package alcoholboot.toastit.feature.categorysearch.repository;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.entity.CocktailDocument;
import alcoholboot.toastit.feature.categorysearch.repository.custom.CustomCocktailRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CocktailRepository extends MongoRepository<CocktailDocument, ObjectId>, CustomCocktailRepository {
    List<CocktailDocument> findByStrGlass(String glass);
    List<CocktailDocument> findByStrCategory(String category);
}