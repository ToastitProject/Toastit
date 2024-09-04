package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

import java.util.List;

/**
 * 날씨 정보들을 담고있는 DTO
 * numOFRows : 페이지당 나오는 데이터 수
 * pageNo : 페이지 수
 * totalCount :데이터 총 개수
 */

@Data
public class WeatherItemsDTO {
    private List<WeatherItemDTO> item;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
