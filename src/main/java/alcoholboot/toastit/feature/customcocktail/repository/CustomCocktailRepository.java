package alcoholboot.toastit.feature.customcocktail.repository;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomCocktailRepository extends CrudRepository<CustomCocktail, Long> {
Optional<UserEntity> findUserByName(String username);
CustomCocktail findIdByName(String cocktailName);

//팔루우 한 id 들로 작성된 커스텀 칵테일 레시피들을 모두 죄회하는 쿼리문
@Query("SELECT c FROM CustomCocktail c WHERE c.user.id IN :ids")
List<CustomCocktail> getCocktailsByUserIds(@Param("ids") List<Long> ids);
}
