package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

/**
 * 기상청 api의 응답 메시지 전체.
 * <p>
 * WeatherResponseDTO로 이어진다.
 */
@Data
public class WeatherApiResponseDTO {
    private WeatherResponseDTO response;
}
