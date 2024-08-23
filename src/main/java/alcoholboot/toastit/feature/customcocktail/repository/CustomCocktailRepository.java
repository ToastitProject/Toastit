package alcoholboot.toastit.feature.customcocktail.repository;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomCocktailRepository extends CrudRepository<CustomCocktail, Long> {
Optional<UserEntity> findUserByName(String username);
CustomCocktail findIdByName(String cocktailName);

//팔루우 한 id 들로 작성된 커스텀 칵테일 레시피들을 모두 죄회하는 쿼리문
@Query("SELECT c FROM CustomCocktail c WHERE c.user.id IN :ids")
List<CustomCocktail> getCocktailsByUserIds(@Param("ids") List<Long> ids);

//커스텀 칵테일을 등록 순서대로 가져오는 쿼리문 -> 서비스 클래스에서 상위 n개 선택 할 수 있음
@Query("SELECT c FROM CustomCocktail c ORDER BY c.createDate DESC")
List<CustomCocktail> findTop5ByOrderByCreateDateDesc(Pageable pageable);

//커스텀 칵테일을 좋아요 순서대로 가져오는 쿼리문 -> 서비스 클래스에서 상위 n개 선택 할 수 있음
@Query("SELECT c FROM CustomCocktail c JOIN c.likes l GROUP BY c.id ORDER BY COUNT(l.id) DESC")
List<CustomCocktail> findTopByOrderByLikesDesc(Pageable pageable);

//커스텀 칵테일 중 팔로우가 많은 사용자의 레시피를 순서대로 가져오는 쿼리문 -> 서비스 클래스에서 상위 n개 선택할 수 있음
@Query("SELECT c FROM CustomCocktail c " +
        "LEFT JOIN FollowEntity f ON f.followee.id = c.id " +
        "GROUP BY c.id " +
        "ORDER BY COUNT(f.id) DESC")
List<CustomCocktail> findTopByOrderByFollowerCountDesc(Pageable pageable);
}




