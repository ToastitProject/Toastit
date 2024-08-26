package alcoholboot.toastit.feature.api.controller;

import alcoholboot.toastit.feature.api.dto.AreaRequestDTO;
import alcoholboot.toastit.feature.api.dto.LatXLngY;
import alcoholboot.toastit.feature.api.dto.WeatherDTO;
import alcoholboot.toastit.feature.api.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AreaWeatherController {
    private final WeatherService weatherService;

    @Value("${google.maps.api.key}")
    private String mapsApiKey;

    @Value("${google.geocoding.api.key}")
    private String geocodingApiKey;


    @GetMapping("/areaweather")
    public String map(Model model) {
        log.info("map 으로 GetMapping 이 들어옴");
        model.addAttribute("mapsApiKey", mapsApiKey);
        model.addAttribute("geocodingApiKey", geocodingApiKey);
//        log.info("모델에 담아 보내는 MAP API KEY : "+ mapsApiKey);
//        log.info("모델에 담아 보내는 Geocoding API KEY : "+geocodingApiKey);
        return "feature/api/areaweather";
    }

    @PostMapping("/save-coordinates")
    public String saveCoordinates(@RequestBody Map<String, Double> coordinates) throws UnsupportedEncodingException, URISyntaxException, JsonProcessingException {
        Double latitude = coordinates.get("latitude");
        Double longitude = coordinates.get("longitude");

        // 받은 위도와 경도를 한번 표시해보기
//        System.out.println("latitude: " + latitude + " longitude: " + longitude);

        // 위도와 경도를 격자 x,y 좌표로 변환하기
        LatXLngY latXLngY = weatherService.convertGRID_GPS(0, latitude, longitude);
//       System.out.println("x,y : "+latXLngY.getX()+","+latXLngY.getY());

        // 변환한 x,y좌표로 api요청을 보내서 날씨 정보를 받기
        // areacode, 날짜, 시간(1시간 단위) 추가로 필요
        AreaRequestDTO areaRequestDTO = new AreaRequestDTO();
        areaRequestDTO.setNx(latXLngY.getStringX());
        areaRequestDTO.setNy(latXLngY.getStringY());

        // areacode 받아와보기
        //System.out.println("areacode 테스트 : "+weatherService.getAreaCode(latXLngY.getStringX(), latXLngY.getStringY()));

        // areacode 추가
        areaRequestDTO.setAreacode(weatherService.getAreaCode(latXLngY.getStringX(), latXLngY.getStringY()).getFirst());

        // 날짜 받아와보기
        LocalDate now = LocalDate.now();
        String basedate = now.getYear() + "";
        if (now.getMonthValue() < 10) {
            basedate += "0" + now.getMonthValue();
        } else {
            basedate += now.getMonthValue();
        }

        if (now.getDayOfMonth() < 10) {
            basedate += "0" + now.getDayOfMonth();
        } else {
            basedate += now.getDayOfMonth();
        }

        //System.out.println("basedate test : " + basedate);

        // 날짜 추가
        areaRequestDTO.setBaseDate(basedate);

        // 시간 받아와보기
        LocalTime nowTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH0000");
        String basetime = nowTime.format(formatter);

        //System.out.println("basetime test : " + basetime);

        // 시간 추가
        areaRequestDTO.setBaseTime(basetime);

        List<WeatherDTO> weatherDTOList = weatherService.getWeather(areaRequestDTO);

        // 받은 날씨 정보에서 기온과 기상형태를 받기
        // System.out.println(weatherDTOList);

        return "/feature/api/areaweather";
    }
}
