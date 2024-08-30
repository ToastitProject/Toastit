package alcoholboot.toastit.feature.craftcocktail.service;

import alcoholboot.toastit.feature.craftcocktail.domain.CraftCocktail;
import alcoholboot.toastit.feature.craftcocktail.repository.CustomCocktailRepository;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service
public class CraftCocktailService {

    private final CustomCocktailRepository customCocktailRepository;

    @Autowired
    public CraftCocktailService(CustomCocktailRepository customCocktailRepository) {
        this.customCocktailRepository = customCocktailRepository;
    }

    public List<CraftCocktail> getAllCocktails() {
        return (List<CraftCocktail>) customCocktailRepository.findAll();
    }

    public void saveCocktail(CraftCocktail customCocktail) {
        customCocktailRepository.save(customCocktail);
    }

    public CraftCocktail getCocktailById(Long id) {
        Optional<CraftCocktail> cocktail = customCocktailRepository.findById(id);
        return cocktail.orElseThrow(() -> new RuntimeException("Cocktail not found"));
    }

    public Optional<UserEntity> findUserByName(String username) {
        return customCocktailRepository.findUserByName(username);
    }

    public CraftCocktail findIdByName(String cocktailName) {
        return customCocktailRepository.findIdByName(cocktailName);
    }

    public void deleteCocktail(Long id) {
        customCocktailRepository.deleteById(id);
    }

    public List<CraftCocktail> getCocktailsByUserIds(List<Long> ids) {
        return customCocktailRepository.getCocktailsByUserIds(ids);
    }

    //등록된 커스텀 레시피 중, 최신 등록된 레시피를 limit 개 가져온다.
    public List<CraftCocktail> getLatestCocktails(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customCocktailRepository.findTop5ByOrderByCreateDateDesc(pageable);
    }
    //등록된 커스텀 레시피 중, 좋아요를 많이 받은 레시피를 limit 개 가져온다.
    public List<CraftCocktail> getTopNCocktails(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customCocktailRepository.findTopByOrderByLikesDesc(pageable);
    }
    //등록된 커스텀 레시피 중, 팔로우가 많이 된 사용자들의 레시피를 limit 개 가져온다.
    public List<CraftCocktail> getTopNCocktailsByFollowerCount(int limit) {
        Pageable pageable = PageRequest.of(0, limit); // 페이지 번호 0, 가져올 수 limit
        return customCocktailRepository.findTopByOrderByFollowerCountDesc(pageable);
    }
}

