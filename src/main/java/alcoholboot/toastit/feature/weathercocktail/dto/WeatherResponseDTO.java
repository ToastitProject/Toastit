package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

/**
 * 응답으로 온 WeatherApiResponseDTO를 header와 body로 나눈 것
 * WeatherHeaderDTO와 WeatherBodyDTO로 나눠진다.
 */
@Data
public class WeatherResponseDTO {
    private WeatherHeaderDTO header;
    private WeatherBodyDTO body;
}
