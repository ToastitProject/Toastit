package alcoholboot.toastit.feature.categorysearch.repository;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CocktailRepository extends MongoRepository<Cocktail, String> {
    Page<Cocktail> findByStrIngredientsContaining(String ingredient, Pageable pageable);
    Page<Cocktail> findByStrGlass(String glass, Pageable pageable);
    Page<Cocktail> findByStrCategory(String category, Pageable pageable);
    Page<Cocktail> findByStrIngredientsContainingAndStrGlassAndStrCategory(String ingredient, String glass, String category, Pageable pageable);
}