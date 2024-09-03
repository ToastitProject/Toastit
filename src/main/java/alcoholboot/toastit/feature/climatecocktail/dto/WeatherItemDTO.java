package alcoholboot.toastit.feature.climatecocktail.dto;

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