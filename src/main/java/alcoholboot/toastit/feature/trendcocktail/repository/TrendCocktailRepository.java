package alcoholboot.toastit.feature.trendcocktail.repository;

import alcoholboot.toastit.feature.trendcocktail.entity.TrendCocktail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TrendCocktailRepository extends JpaRepository<TrendCocktail, Long> {

    @Query(value = "SELECT t FROM TrendCocktail t ORDER BY (t.searchVolumeTwoMonthAgo - t.searchVolumeOneMonthAgo) DESC")
    List<TrendCocktail> findTop5BySearchVolume();
}