package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

/**
 * response에 포함된 header 정보
 * <p>
 * 응답 코드와 메시지는 응답이 정상인지, 에러가 났다면 어디서 났는지 정보를 알려줌
 */
@Data
public class WeatherHeaderDTO {
    // 응답 코드
    private String resultCode;
    // 응답 메시지
    private String resultMsg;
}
