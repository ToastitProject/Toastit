package alcoholboot.toastit.feature.craftcocktail.service;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;

import java.util.List;
import java.util.Optional;

/**
 * 입력 레시피 칵테일 서비스를 위한 인터페이스
 */
public interface CraftCocktailService {

    List<CraftCocktailEntity> getAllCocktails();

    void saveCocktail(CraftCocktailEntity customCocktail);

    CraftCocktailEntity getCocktailById(Long id);

    Optional<UserEntity> findUserByName(String username);

    CraftCocktailEntity findIdByName(String cocktailName);

    void deleteCocktail(Long id);

    List<CraftCocktailEntity> getCocktailsByUserIds(List<Long> ids);

    List<CraftCocktailEntity> getLatestCocktails(int limit);

    List<CraftCocktailEntity> getTopNCocktails(int limit);

    List<CraftCocktailEntity> getTopNCocktailsByFollowerCount(int limit);

    List<CraftCocktailEntity> getCocktailsByUserId(Long userId);
}