package alcoholboot.toastit.feature.categorysearch.repository;


import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CocktailRepository extends MongoRepository<Cocktail, String> {
    List<Cocktail> findByStrCategory(String category);
    List<Cocktail> findByStrIngredientsContaining(String ingredient);
    List<Cocktail> findByStrGlass(String glass);
    List<Cocktail> findByStrIngredientsContainingAndStrGlassAndStrCategory(String ingredient, String glass, String category);
}