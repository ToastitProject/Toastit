package alcoholboot.toastit.feature.defaultcocktail.entity;

import alcoholboot.toastit.feature.defaultcocktail.domain.Cocktail;
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

    @Field("strIngredient7")
    private String strIngredient7;

    @Field("strIngredient8")
    private String strIngredient8;

    @Field("strIngredient9")
    private String strIngredient9;

    @Field("strIngredient10")
    private String strIngredient10;

    @Field("strIngredient11")
    private String strIngredient11;

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

    @Field("strMeasure7")
    private String strMeasure7;

    @Field("strMeasure8")
    private String strMeasure8;

    @Field("strMeasure9")
    private String strMeasure9;

    @Field("strMeasure10")
    private String strMeasure10;

    @Field("strMeasure11")
    private String strMeasure11;

    @Field("imagePath")
    private String imagePath;

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
                .strIngredient7(this.strIngredient7)
                .strIngredient8(this.strIngredient8)
                .strIngredient9(this.strIngredient9)
                .strIngredient10(this.strIngredient10)
                .strIngredient11(this.strIngredient11)
                .strInstructions(this.strInstructions)
                .strMeasure1(this.strMeasure1)
                .strMeasure2(this.strMeasure2)
                .strMeasure3(this.strMeasure3)
                .strMeasure4(this.strMeasure4)
                .strMeasure5(this.strMeasure5)
                .strMeasure6(this.strMeasure6)
                .strMeasure7(this.strMeasure7)
                .strMeasure8(this.strMeasure8)
                .strMeasure9(this.strMeasure9)
                .strMeasure10(this.strMeasure10)
                .strMeasure11(this.strMeasure11)
                .likeCount(this.likeCount)
                .imagePath(this.imagePath)
                .build();
    }
}