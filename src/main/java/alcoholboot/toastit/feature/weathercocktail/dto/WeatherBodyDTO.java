package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

/**
 * response에 포함된 body 정보
 * <p>
 * datatype은 JSON, XML중의 하나이고
 * items에는 날씨 정보들이 들어있다.
 */
@Data
public class WeatherBodyDTO {
    private String dataType;
    private WeatherItemsDTO items;
}