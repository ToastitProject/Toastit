package alcoholboot.toastit.feature.custom.repository;

import alcoholboot.toastit.feature.custom.domain.CustomCocktail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCocktailRepository extends CrudRepository<CustomCocktail, Long> {

}
