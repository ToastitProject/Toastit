package alcoholboot.toastit.feature.localcocktail.repository;


import alcoholboot.toastit.feature.localcocktail.entity.LocationCocktail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCocktailRepository extends JpaRepository<LocationCocktail, Long> {

    @Query("SELECT r.ingredient1, r.ingredient2, r.ingredient3, r.ingredient4 " +
            "FROM LocationCocktail r " +
            "WHERE r.si = :si " +
            "AND (:deo IS NULL OR r.deo = :deo)")
    String findIngredientsBySiAndDeo(@Param("si") String si, @Param("deo") String deo);
}