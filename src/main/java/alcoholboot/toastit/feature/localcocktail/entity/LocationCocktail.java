package alcoholboot.toastit.feature.localcocktail.entity;

import jakarta.persistence.*;

import lombok.Getter;

/**
 * 위치 기반 칵테일을 나타내는 엔티티
 */
@Getter
@Entity
@Table(name = "location_cocktail")
public class LocationCocktail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 시 정보
    private String si;

    // 도 정보
    private String deo;

    // 첫 번째 재료
    private String ingredient1;

    // 두 번째 재료
    private String ingredient2;

    // 세 번째 재료
    private String ingredient3;

    // 네 번째 재
    private String ingredient4;
}