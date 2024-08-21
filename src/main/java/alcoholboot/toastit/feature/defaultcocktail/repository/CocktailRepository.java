package alcoholboot.toastit.feature.defaultcocktail.repository;

import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.defaultcocktail.repository.custom.CustomCocktailRepository;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CocktailRepository extends MongoRepository<CocktailDocument, ObjectId>, CustomCocktailRepository {
    Page<CocktailDocument> findByStrGlass(String glass, Pageable pageable);
    Page<CocktailDocument> findByStrCategory(String category, Pageable pageable);
}