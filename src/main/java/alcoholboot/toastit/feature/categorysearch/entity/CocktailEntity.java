package alcoholboot.toastit.feature.categorysearch.entity;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CocktailEntity {
    @Id
    private ObjectId id;

    @Column(nullable = false)
    private String strDrink;

    @Column(nullable = false)
    private String strAlcoholic;

    @Column(nullable = false)
    private String strCategory;

    @Column(nullable = false)
    private String strGlass;

    @ElementCollection
    private List<String> strIngredients;

    @ElementCollection
    private List<String> strMeasures;

    @Column(nullable = false)
    private String strInstructions;

    // 아직 도메인과 엔티티의 변환을 사용하지 않음. 사용법 고안해야 함
    public Cocktail convertToDomain() {
        return Cocktail.builder()
                .id(this.id)
                .strDrink(this.strDrink)
                .strAlcoholic(this.strAlcoholic)
                .strCategory(this.strCategory)
                .strGlass(this.strGlass)
                .strIngredients(this.strIngredients)
                .strMeasures(this.strMeasures)
                .strInstructions(this.strInstructions)
                .build();
    }
}
