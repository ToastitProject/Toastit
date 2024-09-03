package alcoholboot.toastit.feature.climatecocktail.dto;

import lombok.Data;

@Data
public class WeatherResponseDTO {
    private WeatherHeaderDTO header;
    private WeatherBodyDTO body;
}
