package alcoholboot.toastit.feature.recommendByLocation.Repository;


import alcoholboot.toastit.feature.recommendByLocation.Entity.RecommendByLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendByLocationRepository extends JpaRepository<RecommendByLocation, Long> {

    @Query("SELECT r.ingredient1, r.ingredient2, r.ingredient3, r.ingredient4 " +
            "FROM RecommendByLocation r " +
            "WHERE r.si = :si " +
            "AND (:deo IS NULL OR r.deo = :deo)")
    String findIngredientsBySiAndDeo(@Param("si") String si, @Param("deo") String deo);
}