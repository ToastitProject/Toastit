package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;

import lombok.*;

import org.bson.types.ObjectId;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    private Long id;
    private Long cocktailId;
    private ObjectId basecocktailId;
    private CraftCocktailEntity craftCocktail;
    private UserEntity userEntity;

    public LikeEntity convertToEntity() {
        return LikeEntity.builder()
                .id(this.id)
                .cocktailId(this.cocktailId)
                .basecocktailsId(this.basecocktailId)
                .craftCocktail(this.getCraftCocktail())
                .user(userEntity)
                .build();
    }
}