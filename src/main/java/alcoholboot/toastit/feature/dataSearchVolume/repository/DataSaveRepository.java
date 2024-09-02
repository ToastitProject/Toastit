package alcoholboot.toastit.feature.dataSearchVolume.repository;



import alcoholboot.toastit.feature.dataSearchVolume.entity.CocktailDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSaveRepository extends JpaRepository<CocktailDataEntity, Long> {

}
