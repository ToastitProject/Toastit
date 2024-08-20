package alcoholboot.toastit.feature.customcocktail.repository;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomCocktailRepository extends CrudRepository<CustomCocktail, Long> {
Optional<UserEntity> findUserByName(String username);
CustomCocktail findIdByName(String cocktailName);
}
