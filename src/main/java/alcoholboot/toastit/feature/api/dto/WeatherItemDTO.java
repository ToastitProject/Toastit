package alcoholboot.toastit.feature.api.dto;

import alcoholboot.toastit.feature.api.entity.WeatherEntity;
import lombok.Builder;
import lombok.Data;

@Data
public class WeatherItemDTO {
    private String baseDate;
    private String baseTime;
    private String category;
    private String nx;
    private String ny;
    private Double obsrValue;
}