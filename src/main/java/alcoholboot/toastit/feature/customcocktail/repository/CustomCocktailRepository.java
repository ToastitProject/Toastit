package alcoholboot.toastit.feature.customcocktail.repository;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomCocktailRepository extends CrudRepository<CustomCocktail, Long> {

}
