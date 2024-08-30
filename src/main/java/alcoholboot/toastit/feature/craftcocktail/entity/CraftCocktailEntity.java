package alcoholboot.toastit.feature.craftcocktail.entity;

import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.global.entity.JpaAuditingFields;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "craft_cocktails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CraftCocktailEntity extends JpaAuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "recipe", nullable = false)
    private String recipe;

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL)
    private List<ImageEntity> images = new ArrayList<>();

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL)
    private List<IngredientEntity> ingredients = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "customCocktail", cascade = CascadeType.ALL)
    private List<LikeEntity> likes = new ArrayList<>();


    public static CraftCocktailEntity createCocktail(UserEntity user, String name, String description, String recipe) {
        CraftCocktailEntity cocktail = new CraftCocktailEntity();
        cocktail.setUser(user);
        cocktail.setName(name);
        cocktail.setDescription(description);
        cocktail.setRecipe(recipe);
        return cocktail;
    }

    public void addIngredient(IngredientEntity ingredientEntity) {
        ingredients.add(ingredientEntity);
        ingredientEntity.setCocktail(this);
    }

    public void removeIngredient(IngredientEntity ingredientEntity) {
        ingredients.remove(ingredientEntity);
        ingredientEntity.setCocktail(null);
    }
}