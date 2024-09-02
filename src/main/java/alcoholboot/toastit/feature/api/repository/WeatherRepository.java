package alcoholboot.toastit.feature.api.repository;

import alcoholboot.toastit.feature.api.dto.AreaRequestDTO;
import alcoholboot.toastit.feature.api.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    @Query("select a.areacode from AreaEntity a where a.gridx=:nx and a.gridy=:ny")
    List<String> selectAreaCode(@Param(value = "nx") String nx, @Param(value = "ny") String ny);

    @Query("select a.areacode from AreaEntity a where a.areacode=:areacode")
    AreaRequestDTO selectCoordinate(@Param(value = "areacode") String areacode);

    @Query("select distinct w.baseDate, w.baseTime, w.category, w.nx, w.ny, w.obsrValue " +
            "from WeatherEntity w " +
            "where w.baseDate=:#{#areaRequestDTO.baseDate} and w.baseTime=:#{#areaRequestDTO.baseTime} and w.nx=:#{#areaRequestDTO.nx} and w.ny=:#{#areaRequestDTO.ny}")
    List<WeatherEntity> selectSameCoordinateWeatherList(@Param(value = "areaRequestDTO") AreaRequestDTO areaRequestDTO);

}
