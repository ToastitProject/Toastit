package alcoholboot.toastit.feature.weathercocktail.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * 기상청에서 제공하는 엑셀 파일을 db로 옮긴것
 */

@Getter
@Setter
@Entity
@Table(name = "weather_area")
public class AreaEntity {
    @Size(max = 255)
    @Column(name = "areacode")
    @Id
    private String areacode; // 행정구역 코드

    @Size(max = 255)
    @Column(name = "step1")
    private String step1; // 시/도

    @Size(max = 255)
    @Column(name = "step2")
    private String step2; // 시/군/구

    @Size(max = 255)
    @Column(name = "step3")
    private String step3; // 읍/면/동

    @Size(max = 255)
    @Column(name = "gridx")
    private String gridx; // x좌표

    @Size(max = 255)
    @Column(name = "gridy")
    private String gridy; // y좌표

    @Size(max = 50)
    @NotNull
    @Column(name = "longitudeHour", nullable = false, length = 50)
    private String longitudeHour; // 경도 시

    @Size(max = 50)
    @NotNull
    @Column(name = "longitudeMin", nullable = false, length = 50)
    private String longitudeMin; // 경도 분

    @Size(max = 50)
    @NotNull
    @Column(name = "longitudeSec", nullable = false, length = 50)
    private String longitudeSec; // 경도 초

    @Size(max = 50)
    @NotNull
    @Column(name = "latitudeHour", nullable = false, length = 50)
    private String latitudeHour; // 위도 시

    @Size(max = 50)
    @NotNull
    @Column(name = "latitudeMin", nullable = false, length = 50)
    private String latitudeMin; // 위도 분

    @Size(max = 50)
    @NotNull
    @Column(name = "latitudeSec", nullable = false, length = 50)
    private String latitudeSec; // 위도 초

    @Size(max = 50)
    @NotNull
    @Column(name = "longitudeMs", nullable = false, length = 50)
    private String longitudeMs; // 경도 ms

    @Size(max = 50)
    @NotNull
    @Column(name = "latitudeMs", nullable = false, length = 50)
    private String latitudeMs; //위도 ms

}