package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.user.domain.Like;
import alcoholboot.toastit.global.Entity.AuditingFields;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class LikeEntity extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cocktail_id",nullable = false)
    private String cocktailId;

    @Column(name = "custom_cocktail_id",nullable = false)
    private String customCocktailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private UserEntity user;

    public Like convertToDomain(){
        return Like.builder()
                .id(this.id)
                .cocktailId(this.cocktailId)
                .customCocktailId(this.customCocktailId)
                .build();
    }

    }

