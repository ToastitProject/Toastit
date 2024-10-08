package alcoholboot.toastit.feature.weathercocktail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 날씨에 맞는 재료들을 분류해서 넣어둔 db
 */

@Entity
@Table(name = "weather_cocktails")
@Getter
@Setter
public class WeatherCocktailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 추움, 시원함, 따뜻함, 더움
    // 맑음, 비, 눈
    private String weather;
    private String ingredient1;
    private String ingredient2;
    private String ingredient3;
    private String ingredient4;
    private String ingredient5;
    private String ingredient6;
    private String ingredient7;
    private String ingredient8;
    private String ingredient9;
}
