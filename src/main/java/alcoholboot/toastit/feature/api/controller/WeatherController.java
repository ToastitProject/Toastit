package alcoholboot.toastit.feature.api.controller;

import alcoholboot.toastit.feature.api.dto.AreaRequestDTO;
import alcoholboot.toastit.feature.api.dto.WeatherDTO;
import alcoholboot.toastit.feature.api.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

@Controller
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public String weather() {
        return "feature/api/weather";
    }

    @PostMapping("/weatherArea")
    @ResponseBody
    public List<AreaRequestDTO> getAreaStep(@RequestParam Map<String, String> params) {
        return this.weatherService.getArea(params);
    }

    @PostMapping(value = "/getWeather")
    @ResponseBody
    public List<WeatherDTO> getWeatherInfo(@ModelAttribute AreaRequestDTO areaRequestDTO) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException, URISyntaxException {
        AreaRequestDTO coordinate = this.weatherService.getCoordinate(areaRequestDTO.getAreacode());
        areaRequestDTO.setNx(coordinate.getNx());
        areaRequestDTO.setNy(coordinate.getNy());

        List<WeatherDTO> weatherDTOList = this.weatherService.getWeather(areaRequestDTO);

        return weatherDTOList;
    }
}
