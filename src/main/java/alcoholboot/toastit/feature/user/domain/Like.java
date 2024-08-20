package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    private Long id;
    private Long cocktailId;
    private CustomCocktail customCocktail;
    private UserEntity userEntity;

    public LikeEntity convertToEntity() {
        return LikeEntity.builder()
                .id(this.id)
                .cocktailId(this.cocktailId)
                .customCocktail(this.getCustomCocktail())
                .user(userEntity)
                .build();
    }
}