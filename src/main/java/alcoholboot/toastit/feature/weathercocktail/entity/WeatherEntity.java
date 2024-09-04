package alcoholboot.toastit.feature.weathercocktail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 기상청 api로 받은 정보를 저장하는 db
 */

@Entity
@Table(name = "weather_response")
@Getter
@Setter
public class WeatherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "baseDate")
    private String baseDate; // 날짜

    @Column(name = "baseTime")
    private String baseTime; // 시간

    @Column(name = "category")
    private String category; // 카테고리

    @Column(name = "nx")
    private String nx; // x좌표

    @Column(name = "ny")
    private String ny; // y좌표

    @Column(name = "obsrValue")
    private String obsrValue; // 관측 값
}
