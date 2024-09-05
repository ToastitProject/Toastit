package alcoholboot.toastit.feature.weathercocktail.repository;

import alcoholboot.toastit.feature.weathercocktail.dto.AreaRequestDTO;
import alcoholboot.toastit.feature.weathercocktail.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    /**
     * xy좌표를 통해서 행정구역코드를 찾는 메소드.
     *
     * @param nx
     * @param ny
     * @return
     */
    @Query("select a.areacode from AreaEntity a where a.gridx=:nx and a.gridy=:ny")
    List<String> selectAreaCode(@Param(value = "nx") String nx, @Param(value = "ny") String ny);

    /**
     * areaRequestDTO를 통해서 같은 조건으로 기상청 api에 요청을 보낸적이 있는지 확인하는 메소드.
     *
     * @param areaRequestDTO 기상청 api에 요청을 보낼때 사용하는 DTO
     * @return
     */
    @Query("select distinct w.baseDate, w.baseTime, w.category, w.nx, w.ny, w.obsrValue " +
            "from WeatherEntity w " +
            "where w.baseDate=:#{#areaRequestDTO.baseDate} and w.baseTime=:#{#areaRequestDTO.baseTime} and w.nx=:#{#areaRequestDTO.nx} and w.ny=:#{#areaRequestDTO.ny}")
    List<WeatherEntity> selectSameCoordinateWeatherList(@Param(value = "areaRequestDTO") AreaRequestDTO areaRequestDTO);

    /**
     * 원하는 날짜의 날씨 데이터들을 반환하는 메소드.
     *
     * @param baseDate
     * @return
     */
    @Query("select w from WeatherEntity w where w.baseDate=:baseDate")
    List<WeatherEntity> selectWeatherListByBaseDate(@Param(value = "baseDate") String baseDate);
}
