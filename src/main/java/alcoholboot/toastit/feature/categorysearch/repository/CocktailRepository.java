package alcoholboot.toastit.feature.categorysearch.repository;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CocktailRepository extends MongoRepository<CocktailEntity, String> {
    Page<CocktailEntity> findByStrIngredientsContaining(String ingredient, Pageable pageable);
    Page<CocktailEntity> findByStrGlass(String glass, Pageable pageable);
    Page<CocktailEntity> findByStrCategory(String category, Pageable pageable);
    Page<CocktailEntity> findByStrIngredientsContainingAndStrGlassAndStrCategory(String ingredient, String glass, String category, Pageable pageable);
}