package alcoholboot.toastit.feature.craftcocktail.service.impl;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.entity.IngredientEntity;
import alcoholboot.toastit.feature.craftcocktail.repository.CraftCocktailRepository;
import alcoholboot.toastit.feature.craftcocktail.service.CraftCocktailService;
import alcoholboot.toastit.feature.user.entity.UserEntity;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CraftCocktailServiceImpl implements CraftCocktailService {

    private final CraftCocktailRepository craftCocktailRepository;

    @Override
    @Transactional
    public void saveCocktail(CraftCocktailEntity customCocktail) {
        for (IngredientEntity ingredient : customCocktail.getIngredients()) {
            ingredient.setCocktail(customCocktail);
        }

        craftCocktailRepository.save(customCocktail);
    }

    @Override
    public CraftCocktailEntity getCocktailById(Long id) {
        Optional<CraftCocktailEntity> cocktail = craftCocktailRepository.findById(id);
        return cocktail.orElseThrow(() -> new RuntimeException("Cocktail not found"));
    }

    @Override
    public List<CraftCocktailEntity> getAllCocktails() {
        return craftCocktailRepository.findAll();
    }

    @Override
    public Optional<UserEntity> findUserByName(String username) {
        return craftCocktailRepository.findUserByName(username);
    }

    @Override
    public CraftCocktailEntity findIdByName(String cocktailName) {
        return craftCocktailRepository.findIdByName(cocktailName);
    }

    @Override
    public void deleteCocktail(Long id) {
        craftCocktailRepository.deleteById(id);
    }

    @Override
    public List<CraftCocktailEntity> getCocktailsByUserIds(List<Long> ids) {
        return craftCocktailRepository.getCocktailsByUserIds(ids);
    }

    @Override
    public List<CraftCocktailEntity> getLatestCocktails(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return craftCocktailRepository.findTop5ByOrderByCreateDateDesc(pageable);
    }

    @Override
    public List<CraftCocktailEntity> getTopNCocktails(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return craftCocktailRepository.findTopByOrderByLikesDesc(pageable);
    }

    @Override
    public List<CraftCocktailEntity> getTopNCocktailsByFollowerCount(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return craftCocktailRepository.findTopByOrderByFollowerCountDesc(pageable);
    }

    @Override
    public List<CraftCocktailEntity> getCocktailsByUserId(Long userId) {
        return craftCocktailRepository.getCocktailsByUserId(userId);
    }
}
