package alcoholboot.toastit.feature.categorysearch.entity;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "cocktails")
public class CocktailDocument {
    @Id
    @Field("_id")
    private ObjectId id;

    @Field("strDrink")
    private String strDrink;

    @NotNull
    @Field("strAlcoholic")
    private String strAlcoholic;

    @NotNull
    @Field("strCategory")
    private String strCategory;

    @NotNull
    @Field("strGlass")
    private String strGlass;

    @Field("strIngredient1")
    private String strIngredient1;

    @Field("strIngredient2")
    private String strIngredient2;

    @Field("strIngredient3")
    private String strIngredient3;

    @Field("strIngredient4")
    private String strIngredient4;

    @Field("strIngredient5")
    private String strIngredient5;

    @Field("strIngredient6")
    private String strIngredient6;

    @Field("strInstructions")
    private String strInstructions;

    @Field("strMeasure1")
    private String strMeasure1;

    @Field("strMeasure2")
    private String strMeasure2;

    @Field("strMeasure3")
    private String strMeasure3;

    @Field("strMeasure4")
    private String strMeasure4;

    @Field("strMeasure5")
    private String strMeasure5;

    @Field("strMeasure6")
    private String strMeasure6;

    @Field("likeCount")
    private Integer likeCount;

    // 아직 도메인과 엔티티의 변환을 사용하지 않음. 사용법 고안해야 함
    public Cocktail convertToDomain() {
        return Cocktail.builder()
                .id(this.id.toString())
                .strDrink(this.strDrink)
                .strAlcoholic(this.strAlcoholic)
                .strCategory(this.strCategory)
                .strGlass(this.strGlass)
                .strIngredient1(this.strIngredient1)
                .strIngredient2(this.strIngredient2)
                .strIngredient3(this.strIngredient3)
                .strIngredient4(this.strIngredient4)
                .strIngredient5(this.strIngredient5)
                .strIngredient6(this.strIngredient6)
                .strInstructions(this.strInstructions)
                .strMeasure1(this.strMeasure1)
                .strMeasure2(this.strMeasure2)
                .strMeasure3(this.strMeasure3)
                .strMeasure4(this.strMeasure4)
                .strMeasure5(this.strMeasure5)
                .strMeasure6(this.strMeasure6)
                .likeCount(this.likeCount)
                .build();
    }
}