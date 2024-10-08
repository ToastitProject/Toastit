package alcoholboot.toastit.feature.weathercocktail.service;

import alcoholboot.toastit.feature.weathercocktail.dto.*;
import alcoholboot.toastit.feature.weathercocktail.entity.WeatherEntity;
import alcoholboot.toastit.feature.weathercocktail.repository.WeatherRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;

    /**
     * xy좌표를 통해서 행정구역코드를 반환하는 메소드.
     *
     * @param nx
     * @param ny
     * @return
     */
    public List<String> getAreaCode(String nx, String ny) {
        return weatherRepository.selectAreaCode(nx, ny);
    }

    /**
     * AreaRequestDTO를 통해 날씨 정보를 얻는 메소드.
     * weather_response 테이블에 미리 저장해둔 데이터가 있으면, 즉 같은 위치와 시간에서
     * 이미 날씨 정보를 요청한 적이 있으면 저장해둔 데이터를 반환.
     * 저장해둔 데이터가 없으면 새로운 api 요청을 보내서 날씨 데이터를 받아오고 저장한 후에
     * 반환한다.
     *
     * @param areaRequestDTO
     * @return
     */

    public List<WeatherEntity> getWeather(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException, JsonMappingException, JsonProcessingException {
        // 날짜, 시간, nx, ny가 areaRequestDTO의 값과 일치하는 데이터가 있는지 검색
        List<WeatherEntity> weatherList = weatherRepository.selectSameCoordinateWeatherList(areaRequestDTO);

        if (weatherList.isEmpty()) { // 저장된 데이터가 없는 경우 새로 생성
            ResponseEntity<WeatherApiResponseDTO> response = requestWeatherApi(areaRequestDTO);
            ObjectMapper objectMapper = new ObjectMapper();
            List<WeatherItemDTO> weatherItemList = Objects.requireNonNull(response.getBody())
                    .getResponse()
                    .getBody()
                    .getItems()
                    .getItem(); // response에서 weatherItem들을 분류

            for (WeatherItemDTO item : weatherItemList) {
                weatherList.add(objectMapper.readValue(objectMapper.writeValueAsString(item), WeatherEntity.class));
            }
            insertWeatherList(weatherList); // API요청 후 결과값을 DB에 저장

            return weatherList;
        }
        return weatherList;    // DB에 기존 저장되어있는 값에서 가져온 List
    }

//    public AreaRequestDTO getCoordinate(String areacode) {
//        return weatherRepository.selectCoordinate(areacode);
//    }

    /**
     * 기상청에 api요청을 보내고 응답을 반환하는 메소드.
     *
     * @param areaRequestDTO
     * @return
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public ResponseEntity<WeatherApiResponseDTO> requestWeatherApi(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException {
        String url = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
        String serviceKey = "oQqrnh8+bQePPlCtSGERxjrE7EC0LsYNFCsKBIFnWAwf+jDmUcpo+C8cei3NM+WivU8RTBmU6NVY2ntP+mX2UA==";
        String encodedServiceKey = URLEncoder.encode(serviceKey, "UTF-8");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "JSON", StandardCharsets.UTF_8));

        StringBuilder builder = new StringBuilder(url);
        builder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + encodedServiceKey);
        builder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        builder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
        builder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        builder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getBaseDate(), "UTF-8"));
        builder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getBaseTime(), "UTF-8"));
        builder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getNx(), "UTF-8"));
        builder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(areaRequestDTO.getNy(), "UTF-8"));
        URI uri = new URI(builder.toString());

        System.out.println(builder);

        ResponseEntity<WeatherApiResponseDTO> response = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<String>(headers), WeatherApiResponseDTO.class);

        return response;
    }

    /**
     * 위도,경도를 기상청 api에서 쓸 수 있는 x,y 좌표로 변환하는 메소드.
     *
     * @param mode
     * @param lat_X
     * @param lng_Y
     * @return
     */

    public LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y) {
        int TO_GRID = 0;

        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기준점 Y좌표(GRID)


        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        } else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            } else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                } else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }

        rs.intX = (int) rs.x;
        rs.intY = (int) rs.y;

        rs.stringX = String.valueOf(rs.intX);
        rs.stringY = String.valueOf(rs.intY);

        return rs;
    }

    /**
     * 날씨 리스트에서 카테고리에 맞는 정보듦을 반환하는 메소드.
     *
     * @param weatherEntityList
     * @param category
     * @return
     */
    public WeatherEntity getWeatherByCategory(List<WeatherEntity> weatherEntityList, String category) {
        for (WeatherEntity weatherEntity : weatherEntityList) {
            if (weatherEntity.getCategory().equals(category)) {
                return weatherEntity;
            }
        }
        return null;
    }

    /**
     * 날씨 정보를 db에 저장하는 메소드.
     *
     * @param weatherEntityList
     */
    @Transactional
    public void insertWeatherList(List<WeatherEntity> weatherEntityList) {
        weatherRepository.saveAll(weatherEntityList);
    }

    /**
     * 원하는 날짜의 날씨 정보를 db에서 가져오는 메소드.
     *
     * @param baseDate
     * @return
     */
    public List<WeatherEntity> getWeatherByBaseDate(String baseDate) {
        return weatherRepository.selectWeatherListByBaseDate(baseDate);
    }

    /**
     * 날씨 정보들을 db에서 삭제하는 메소드.
     *
     * @param weatherEntityList
     */

    public void deleteWeatherList(List<WeatherEntity> weatherEntityList) {
        weatherRepository.deleteAll(weatherEntityList);
    }
}
