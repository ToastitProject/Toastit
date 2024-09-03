package alcoholboot.toastit.feature.seasonalcocktail.repository;

import alcoholboot.toastit.feature.seasonalcocktail.entity.SeasonalCocktailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeasonalCocktailRepository extends JpaRepository<SeasonalCocktailEntity, Long> {
}
