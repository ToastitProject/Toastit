package alcoholboot.toastit.feature.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "recommend_by_weather")
@Getter
@Setter
public class RecommendByWeatherEntity {
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
