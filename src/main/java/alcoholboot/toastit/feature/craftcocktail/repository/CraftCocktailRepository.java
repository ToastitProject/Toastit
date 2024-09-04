package alcoholboot.toastit.feature.craftcocktail.repository;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface CraftCocktailRepository extends CrudRepository<CraftCocktailEntity, Long> {

    @EntityGraph(attributePaths = {"images", "user"})
    List<CraftCocktailEntity> findAll();

    Optional<UserEntity> findUserByName(String username);

    CraftCocktailEntity findIdByName(String cocktailName);

    //팔루우 한 회원 ID 기준으로 작성된 커스텀 칵테일 레시피들을 모두 죄회하는 쿼리문
    @Query("SELECT c FROM CraftCocktailEntity c WHERE c.user.id IN :ids")
    List<CraftCocktailEntity> getCocktailsByUserIds(@Param("ids") List<Long> ids);

    //커스텀 칵테일을 등록 순서대로 가져오는 쿼리문 -> 서비스 클래스에서 상위 n개 선택 할 수 있음
    @Query("SELECT c FROM CraftCocktailEntity c ORDER BY c.createDate DESC")
    List<CraftCocktailEntity> findTop5ByOrderByCreateDateDesc(Pageable pageable);

    //커스텀 칵테일을 좋아요 순서대로 가져오는 쿼리문 -> 서비스 클래스에서 상위 n개 선택 할 수 있음
    @Query("SELECT c FROM CraftCocktailEntity c JOIN c.likes l GROUP BY c.id ORDER BY COUNT(l.id) DESC")
    List<CraftCocktailEntity> findTopByOrderByLikesDesc(Pageable pageable);

    //커스텀 칵테일 중 팔로우가 많은 사용자의 레시피를 순서대로 가져오는 쿼리문 -> 서비스 클래스에서 상위 n개 선택할 수 있음
    @Query("""
            SELECT c FROM CraftCocktailEntity c
            LEFT JOIN FollowEntity f ON f.followee.id = c.id
            GROUP BY c.id
            ORDER BY COUNT(f.id) DESC
            """)
    List<CraftCocktailEntity> findTopByOrderByFollowerCountDesc(Pageable pageable);

    List<CraftCocktailEntity> getCocktailsByUserId(long userId);
}