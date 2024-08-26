package alcoholboot.toastit.feature.api.service;

import alcoholboot.toastit.feature.api.dto.AreaRequestDTO;
import alcoholboot.toastit.feature.api.dto.LatXLngY;
import alcoholboot.toastit.feature.api.dto.WeatherApiResponseDTO;
import alcoholboot.toastit.feature.api.dto.WeatherDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface WeatherService {
    List<AreaRequestDTO> getArea(Map<String, String> params);

    List<String> getAreaCode(String nx, String ny);

    List<WeatherDTO> getWeather(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException, JsonMappingException, JsonProcessingException;

    AreaRequestDTO getCoordinate(String areacode);

    ResponseEntity<WeatherApiResponseDTO> requestWeatherApi(AreaRequestDTO areaRequestDTO) throws UnsupportedEncodingException, URISyntaxException;

    LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y);
}
