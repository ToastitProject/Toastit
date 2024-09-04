package alcoholboot.toastit.feature.basecocktail.repository;

import alcoholboot.toastit.feature.basecocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.basecocktail.repository.custom.CustomCocktailRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

// do not delete
public interface CocktailRepository extends MongoRepository<CocktailDocument, ObjectId>, CustomCocktailRepository {
    @Query("{ 'id': { $in: ?0 } }")
    List<CocktailDocument> findByIdIn(List<ObjectId> ids);
}