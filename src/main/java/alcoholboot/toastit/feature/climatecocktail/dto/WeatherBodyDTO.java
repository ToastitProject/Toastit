package alcoholboot.toastit.feature.climatecocktail.dto;

import lombok.Data;

@Data
public class WeatherBodyDTO {
    private String dataType;
    private WeatherItemsDTO items;
}