package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

@Data
public class WeatherBodyDTO {
    private String dataType;
    private WeatherItemsDTO items;
}