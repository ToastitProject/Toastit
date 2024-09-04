package alcoholboot.toastit.feature.craftcocktail.domain;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.entity.IngredientEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 입력 레시피 칵테일을 보여주는 도메인
 */
@Getter
@Setter
public class CraftCocktail {

    private Long id;
    private String name;
    private String description;
    private String recipe;
    private UserEntity user;
    private List<IngredientEntity> ingredients;

    public CraftCocktail(Long id, String name, String description, String recipe, UserEntity user, List<IngredientEntity> ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recipe = recipe;
        this.user = user;
        this.ingredients = ingredients;
    }

    public static CraftCocktail createCocktail(UserEntity user, String name, String description, String recipe) {
        return new CraftCocktail(null, name, description, recipe, user, null);
    }
}
