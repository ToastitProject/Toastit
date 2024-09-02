package alcoholboot.toastit.feature.trendcocktail.repository;



import alcoholboot.toastit.feature.trendcocktail.entity.CocktailDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSaveRepository extends JpaRepository<CocktailDataEntity, Long> {

}
