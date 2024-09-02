package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.global.entity.JpaAuditingFields;

import jakarta.persistence.*;

import lombok.*;

import org.bson.types.ObjectId;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "likes")
public class LikeEntity extends JpaAuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cocktailId;

    private ObjectId basecocktailsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "craft_cocktails_id", nullable = true)
    private CraftCocktailEntity craftCocktail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
}