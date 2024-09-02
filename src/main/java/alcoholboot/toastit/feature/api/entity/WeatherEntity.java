package alcoholboot.toastit.feature.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "weather_response")
@Getter
@Setter
public class WeatherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "baseDate")
    private String baseDate;

    @Column(name = "baseTime")
    private String baseTime;

    @Column(name = "category")
    private String category;

    @Column(name = "nx")
    private String nx;

    @Column(name = "ny")
    private String ny;

    @Column(name = "obsrValue")
    private String obsrValue;
}
