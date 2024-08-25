package alcoholboot.toastit.feature.api.dto;

import lombok.Data;

@Data
public class WeatherBodyDTO {
    private String dataType;
    private WeatherItemsDTO items;
}