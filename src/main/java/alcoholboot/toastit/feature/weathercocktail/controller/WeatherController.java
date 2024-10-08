package alcoholboot.toastit.feature.weathercocktail.controller;

import alcoholboot.toastit.feature.weathercocktail.dto.AreaRequestDTO;
import alcoholboot.toastit.feature.weathercocktail.dto.LatXLngY;
import alcoholboot.toastit.feature.weathercocktail.entity.WeatherEntity;
import alcoholboot.toastit.feature.weathercocktail.service.WeatherCocktailService;
import alcoholboot.toastit.feature.weathercocktail.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;
    private final WeatherCocktailService weatherCocktailService;
    private final CocktailService cocktailService;

    @Value("${google.maps.api.key}")
    private String mapsApiKey;

    @Value("${google.geocoding.api.key}")
    private String geocodingApiKey;

    @GetMapping("/weather")
    public String map(Model model) {
        log.debug("map 으로 GetMapping 이 들어옴");
        model.addAttribute("mapsApiKey", mapsApiKey);
        model.addAttribute("geocodingApiKey", geocodingApiKey);

        return "weathercocktail/weathercocktail-view";
    }

    /**
     * 위도와 경도 정보를 받아와서 그것을 기상청 api에서 사용하는 형식의 xy 좌표로 변환
     * xy좌표로 위치 정보를 알아냄, 그리고 날짜와 시간 정보를 추가
     * (위치, 날짜, 시간) 을 이용해서 사용자 위치의 날씨 정보를 받아온 뒤
     * 날씨 정보를 통해 칵테일을 추천
     *
     * @param coordinates 위도와 경도 정보
     * @return
     */
    @PostMapping("/weather")
    @ResponseBody
    public Map<String, Object> saveCoordinates(@RequestBody Map<String, Double> coordinates) throws UnsupportedEncodingException, URISyntaxException, JsonProcessingException {
        Double latitude = coordinates.get("latitude");
        Double longitude = coordinates.get("longitude");

        // 위도와 경도를 격자 x,y 좌표로 변환하기
        LatXLngY latXLngY = weatherService.convertGRID_GPS(0, latitude, longitude);

        // 변환한 x,y좌표로 api요청을 보내서 날씨 정보를 받기
        // areacode, 날짜, 시간(1시간 단위) 추가로 필요
        AreaRequestDTO areaRequestDTO = new AreaRequestDTO();
        areaRequestDTO.setNx(latXLngY.getStringX());
        areaRequestDTO.setNy(latXLngY.getStringY());

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

        // 날짜 추가
        areaRequestDTO.setBaseDate(basedate);

        // 시간 받아와보기
        LocalTime nowTime = LocalTime.now();
        String basetime = "";

        int hour = nowTime.getHour();
        int minute = nowTime.getMinute();

        if (minute < 11) {
            hour--;
            basetime = hour + "0000";
        } else {
            basetime = hour + "0000";
        }

        if (hour < 10) {
            basetime = "0" + basetime;
        }

        // 시간 추가
        areaRequestDTO.setBaseTime(basetime);

        List<WeatherEntity> weatherEntityList = weatherService.getWeather(areaRequestDTO);

        // 받은 날씨 정보에서 기온(T1H)과 기상형태(PTY)를 받기
        String strT1h = weatherService.getWeatherByCategory(weatherEntityList, "T1H").getObsrValue();
        String strPty = weatherService.getWeatherByCategory(weatherEntityList, "PTY").getObsrValue();
        double t1h = Double.parseDouble(strT1h);
        double doublePty = Double.parseDouble(strPty);
        int pty = (int) doublePty;

        // 랜덤으로 쓸 재료를 검색
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int randomInt = random.nextInt(2) + 1;
        String weather = "";
        switch (randomInt) {
            case 1: // t1h
                if (t1h < 8.0) {
                    weather = "cold";
                } else if (t1h < 16.0) {
                    weather = "cool";
                } else if (t1h < 24.0) {
                    weather = "warm";
                } else {
                    weather = "hot";
                }
                break;
            case 2: // pty
                if (pty == 0) {
                    weather = "clear";
                } else if (pty == 1 || pty == 5) {
                    weather = "rain";
                } else {
                    weather = "snow";
                }
                break;
        }

        List<String> ingredients = weatherCocktailService.getIngredientsByWeather(weather);
        randomInt = random.nextInt(9);
        String ingredient = ingredients.get(randomInt);

        // 선택한 재료로 랜덤 칵테일을 선택하기
        List<Cocktail> cocktails = cocktailService.getCocktailsByIngredient(ingredient);
        randomInt = random.nextInt(cocktails.size());

        if (pty == 0) {
            weather = "clear";
        } else if (pty == 1 || pty == 5) {
            weather = "rain";
        } else {
            weather = "snow";
        }

        // response로 해보기
        Map<String, Object> response = new HashMap<>();
        Cocktail selectedCocktail = cocktails.get(randomInt);
        response.put("temperature", t1h);
        response.put("weatherInfo", weather);
        response.put("cocktailInfo", selectedCocktail.getStrDrink());
        response.put("cocktailImage", selectedCocktail.getImagePath());
        response.put("cocktailId", selectedCocktail.getId());

        return response;
    }
}
