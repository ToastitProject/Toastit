package alcoholboot.toastit.feature.amazonimage.domain;

import alcoholboot.toastit.global.Entity.JpaAuditingFields;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes")
public class Like extends JpaAuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cocktail_id",nullable = false)
    private String cocktailId;

    @Column(name = "custom_cocktail_id",nullable = false)
    private String customCocktailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
