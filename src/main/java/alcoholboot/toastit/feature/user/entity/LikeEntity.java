package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.global.entity.JpaAuditingFields;

import jakarta.persistence.*;

import lombok.*;

import org.bson.types.ObjectId;

/**
 * 좋아요 정보를 관리하는 엔티티 클래스.
 * 사용자와 기본 칵테일 또는 커스텀 칵테일 간의 좋아요 관계를 저장합니다.
 */
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
    private Long id;  // 좋아요 관계의 고유 ID

    @Column(name = "base_cocktails_id")
    private ObjectId basecocktailsId;  // MongoDB ObjectId, 기본 칵테일에 대한 좋아요 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "craft_cocktails_id")
    private CraftCocktailEntity craftCocktail;  // 커스텀 칵테일에 대한 좋아요 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private UserEntity user;  // 좋아요를 한 사용자
}