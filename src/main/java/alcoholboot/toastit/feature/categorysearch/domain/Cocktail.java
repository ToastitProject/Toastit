package alcoholboot.toastit.feature.categorysearch.domain;

import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import jakarta.persistence.Id;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "cocktails")
public class Cocktail {
    @Id
    private ObjectId id;
    private String strDrink;
    private String strAlcoholic;
    private String strCategory;
    private String strGlass;
    private List<String> strIngredients;
    private List<String> strMeasures;
    private String strInstructions;
    private Integer likeCount; // 좋아요 갯수를 저장하는 필드 추가

    // 아직 엔티티와 도메인의 변환을 사용하지 않음. 사용법 고안해야 함
    public CocktailEntity convertToEntity() {
        return CocktailEntity.builder()
                .id(this.id)
                .strDrink(this.strDrink)
                .strAlcoholic(this.strAlcoholic)
                .strCategory(this.strCategory)
                .strGlass(this.strGlass)
                .strIngredients(this.strIngredients)
                .strMeasures(this.strMeasures)
                .strInstructions(this.strInstructions)
                .likeCount(this.likeCount)
                .build();

    }
}
