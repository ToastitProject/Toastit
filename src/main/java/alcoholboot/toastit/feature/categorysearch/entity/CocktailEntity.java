package alcoholboot.toastit.feature.categorysearch.entity;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
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

    @Column(nullable = false)
    private Integer likeCount = 0; // 좋아요 개수를 저장하는 필드 추가

    @OneToMany(mappedBy = "cocktail", cascade = CascadeType.ALL) //어떤 user 들이 좋아요를 했는지 확인할 수 있는 자료구조 추가
    private List<LikeEntity> likes = new ArrayList<>();


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
                .likeCount(this.likeCount)
                .build();
    }
}
