package alcoholboot.toastit.feature.weathercocktail.dto;

import lombok.Data;

/**
 * 위/경도를 기상청 api에서 사용하는 형식의 x,y 좌표로 변환하는데 사용
 */
@Data
public class LatXLngY {
    public double lat;
    public double lng;

    public double x;
    public double y;

    public int intX;
    public int intY;

    public String stringX;
    public String stringY;
}
