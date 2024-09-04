package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

/**
 * 날씨 정보를 카테고리별로 나눈 DTO
 * <p>
 * baseDate : 날짜
 * baseTime : 시간
 * category : 카테고리 (습도, 풍향, 풍속, 기온 등...)
 * ny, ny : xy좌표 (위치를 나타내는데 사용)
 * obsrValue : 관측 값. 카테고리에 따라서 의미가 달라짐 (같은 값이라도 습도라면 %, 풍속이라면 m/s)
 */
@Data
public class WeatherItemDTO {
    private String baseDate;
    private String baseTime;
    private String category;
    private String nx;
    private String ny;
    private Double obsrValue;
}