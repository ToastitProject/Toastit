package alcoholboot.toastit.feature.localcocktail.repository;

import alcoholboot.toastit.feature.localcocktail.entity.LocationCocktailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * LocationCocktailEntity 엔티티를 관리하는 레포지토리 인터페이스
 */
@Repository
public interface LocationCocktailRepository extends JpaRepository<LocationCocktailEntity, Long> {

    /**
     * 시와 도에 따라 재료를 조회하는 쿼리
     *
     * @param si 시 정보
     * @param deo 도 정보 (NULL 가능)
     * @return 재료 목록을 포함한 문자열
     */
    @Query("""
            SELECT r.ingredient1, r.ingredient2, r.ingredient3, r.ingredient4
            FROM LocationCocktailEntity r
            WHERE r.si = :si
            AND (:deo IS NULL OR r.deo = :deo)
            """)
    String findIngredientsBySiAndDeo(@Param("si") String si, @Param("deo") String deo);
}