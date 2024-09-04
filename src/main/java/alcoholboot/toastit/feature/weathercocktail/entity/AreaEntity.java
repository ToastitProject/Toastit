package alcoholboot.toastit.feature.weathercocktail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "weather_area")
public class AreaEntity {
    @Size(max = 255)
    @Column(name = "areacode")
    @Id
    private String areacode;

    @Size(max = 255)
    @Column(name = "step1")
    private String step1;

    @Size(max = 255)
    @Column(name = "step2")
    private String step2;

    @Size(max = 255)
    @Column(name = "step3")
    private String step3;

    @Size(max = 255)
    @Column(name = "gridx")
    private String gridx;

    @Size(max = 255)
    @Column(name = "gridy")
    private String gridy;

    @Size(max = 50)
    @NotNull
    @Column(name = "longitudeHour", nullable = false, length = 50)
    private String longitudeHour;

    @Size(max = 50)
    @NotNull
    @Column(name = "longitudeMin", nullable = false, length = 50)
    private String longitudeMin;

    @Size(max = 50)
    @NotNull
    @Column(name = "longitudeSec", nullable = false, length = 50)
    private String longitudeSec;

    @Size(max = 50)
    @NotNull
    @Column(name = "latitudeHour", nullable = false, length = 50)
    private String latitudeHour;

    @Size(max = 50)
    @NotNull
    @Column(name = "latitudeMin", nullable = false, length = 50)
    private String latitudeMin;

    @Size(max = 50)
    @NotNull
    @Column(name = "latitudeSec", nullable = false, length = 50)
    private String latitudeSec;

    @Size(max = 50)
    @NotNull
    @Column(name = "longitudeMs", nullable = false, length = 50)
    private String longitudeMs;

    @Size(max = 50)
    @NotNull
    @Column(name = "latitudeMs", nullable = false, length = 50)
    private String latitudeMs;

}