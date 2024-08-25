package alcoholboot.toastit.feature.api.repository;

import alcoholboot.toastit.feature.api.dto.AreaRequestDTO;
import alcoholboot.toastit.feature.api.dto.WeatherDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WeatherMapper {
    List<AreaRequestDTO> selectArea(Map<String, String> params);

    AreaRequestDTO selectCoordinate(String areacode);

    List<WeatherDTO> selectSameCoordinateWeatherList(AreaRequestDTO areaRequestDTO);

    void insertWeatherList(List<WeatherDTO> weatherList);
}
