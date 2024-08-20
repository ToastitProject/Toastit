package alcoholboot.toastit.feature.customcocktail.domain;

import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.global.Entity.JpaAuditingFields;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cocktail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomCocktail extends JpaAuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "recipe", nullable = false)
    private String recipe;

    @OneToMany(mappedBy = "cocktail",cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "customCocktail", cascade = CascadeType.ALL)
    private List<LikeEntity> likes = new ArrayList<>();


    public static CustomCocktail createCocktail(UserEntity user, String name, String description, String recipe) {
        CustomCocktail cocktail = new CustomCocktail();
        cocktail.setUser(user);
        cocktail.setName(name);
        cocktail.setDescription(description);
        cocktail.setRecipe(recipe);
        return cocktail;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        ingredient.setCocktail(this);
    }

    public void removeIngredient(Ingredient ingredient) {
        ingredients.remove(ingredient);
        ingredient.setCocktail(null);
    }
}
