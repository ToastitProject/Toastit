package alcoholboot.toastit.feature.api.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("WeatherDTO")
public class WeatherDTO {
    private String baseDate;
    private String baseTime;
    private String category;
    private String nx;
    private String ny;
    private Double obsrValue;
}
