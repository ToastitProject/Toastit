package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.domain.Like;
import alcoholboot.toastit.global.entity.JpaAuditingFields;
import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
@Entity
public class LikeEntity extends JpaAuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cocktailId;

    private ObjectId defaultCocktailsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_cocktail_id", nullable = true)
    private CraftCocktailEntity customCocktail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public Like convertToDomain() {
        return Like.builder()
                .id(this.id)
                .cocktailId(cocktailId)
                .defaultCocktailId(this.defaultCocktailsId)
                .customCocktail(this.customCocktail)
                .userEntity(this.user)
                .build();

    }
}