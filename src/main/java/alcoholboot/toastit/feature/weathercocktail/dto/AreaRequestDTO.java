package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

/**
 * 기상청 api를 요청할 때 필요한 정보들
 */
@Data
public class AreaRequestDTO {
    // 행정구역코드
    private String areacode;
    // 시/도
    private String step1;
    // 시/군/구
    private String step2;
    // 읍/면/동
    private String step3;
    // 날짜
    private String baseDate;
    // 시간
    private String baseTime;
    // x좌표
    private String nx;
    // y좌표
    private String ny;
}
