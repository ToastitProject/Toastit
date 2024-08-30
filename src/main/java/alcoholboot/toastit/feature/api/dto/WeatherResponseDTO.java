package alcoholboot.toastit.feature.api.dto;

import lombok.Data;

@Data
public class WeatherResponseDTO {
    private WeatherHeaderDTO header;
    private WeatherBodyDTO body;
}
