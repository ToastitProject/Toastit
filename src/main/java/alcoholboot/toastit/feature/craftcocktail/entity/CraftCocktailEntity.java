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

    // 새 생성자 추가
    public CraftCocktailEntity(Long id, String name, String description, String recipe, UserEntity user, List<IngredientEntity> ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.recipe = recipe;
        this.user = user;
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }

    public void addIngredient(IngredientEntity ingredientEntity) {
        ingredients.add(ingredientEntity);
        ingredientEntity.setCocktail(new CraftCocktailEntity(this.id, this.name, this.description, this.recipe, this.user, this.ingredients));
    }

    public void removeIngredient(IngredientEntity ingredientEntity) {
        ingredients.remove(ingredientEntity);
        ingredientEntity.setCocktail(null);
    }
}
