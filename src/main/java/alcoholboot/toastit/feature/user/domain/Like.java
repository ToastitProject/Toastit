package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.user.entity.LikeEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {
    private Long id;
    private Long userId;
    private String cocktailId;
    private Long customCocktailId;

    public LikeEntity convertToEntity() {
        return LikeEntity.builder()
                .id(this.id)
                .user(this.convertToEntity().getUser())
                .cocktail(this.convertToEntity().getCocktail())
                .customCocktailId(this.customCocktailId)
                .build();
    }


}
