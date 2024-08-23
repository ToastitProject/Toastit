package alcoholboot.toastit.feature.customcocktail.service;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.customcocktail.repository.CustomCocktailRepository;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.Optional;

@Service
public class CustomCocktailService {

    private final CustomCocktailRepository customCocktailRepository;

    @Autowired
    public CustomCocktailService(CustomCocktailRepository customCocktailRepository) {
        this.customCocktailRepository = customCocktailRepository;
    }

    public List<CustomCocktail> getAllCocktails() {
        return (List<CustomCocktail>) customCocktailRepository.findAll();
    }

    public void saveCocktail(CustomCocktail customCocktail) {
        customCocktailRepository.save(customCocktail);
    }

    public CustomCocktail getCocktailById(Long id) {
        Optional<CustomCocktail> cocktail = customCocktailRepository.findById(id);
        return cocktail.orElseThrow(() -> new RuntimeException("Cocktail not found"));
    }

    public Optional<UserEntity> findUserByName(String username) {
        return customCocktailRepository.findUserByName(username);
    }

    public CustomCocktail findIdByName(String cocktailName) {
        return customCocktailRepository.findIdByName(cocktailName);
    }

    public void deleteCocktail(Long id) {
        customCocktailRepository.deleteById(id);
    }

    public List<CustomCocktail> getCocktailsByUserIds(List<Long> ids) {
        return customCocktailRepository.getCocktailsByUserIds(ids);
    }

    //등록된 커스텀 레시피 중, 최신 등록된 레시피를 limit 개 가져온다.
    public List<CustomCocktail> getLatestCocktails(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customCocktailRepository.findTop5ByOrderByCreateDateDesc(pageable);
    }
    //등록된 커스텀 레시피 중, 좋아요를 많이 받은 레시피를 limit 개 가져온다.
    public List<CustomCocktail> getTopNCocktails(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customCocktailRepository.findTopByOrderByLikesDesc(pageable);
    }
    //등록된 커스텀 레시피 중, 팔로우가 많이 된 사용자들의 레시피를 limit 개 가져온다.
    public List<CustomCocktail> getTopNCocktailsByFollowerCount(int limit) {
        Pageable pageable = PageRequest.of(0, limit); // 페이지 번호 0, 가져올 수 limit
        return customCocktailRepository.findTopByOrderByFollowerCountDesc(pageable);
    }
}

