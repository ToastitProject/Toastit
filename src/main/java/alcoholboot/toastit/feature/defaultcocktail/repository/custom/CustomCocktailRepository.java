package alcoholboot.toastit.feature.defaultcocktail.repository.custom;

import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailDocument;

import java.util.List;

public interface CustomCocktailRepository {
    List<CocktailDocument> findCocktailsByIngredient(String ingredient);
    List<CocktailDocument> findByIngredientAndGlassAndCategory(String ingredient, String glass, String category);
}
