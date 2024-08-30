package alcoholboot.toastit.feature.localcocktail.controller.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 위치 데이터를 나타내는 요청 객체
 * 도와 시 정보를 포함
 */
@Getter
@NoArgsConstructor
public class LocationDataRequest {

    // 위치의 도
    private String province;

    // 위치의 시
    private String city;
}