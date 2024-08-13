package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import alcoholboot.toastit.feature.user.domain.Like;
import alcoholboot.toastit.global.Entity.JpaAuditingFields;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "cocktail_id", nullable = false)
    private CocktailEntity cocktail;

    private Long customCocktailId; //구현 예정

    public Like convertToDomain() {
        return Like.builder()
                .id(this.id)
                .userId(this.user.getId())
                .cocktailId(this.cocktail.getId().toHexString())
                .customCocktailId(this.customCocktailId)
                .build();

    }


}
