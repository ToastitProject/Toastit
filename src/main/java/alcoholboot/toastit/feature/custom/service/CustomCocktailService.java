package alcoholboot.toastit.feature.custom.service;

import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.custom.domain.CustomCocktail;
import alcoholboot.toastit.feature.custom.domain.Ingredient;
import alcoholboot.toastit.feature.custom.repository.CustomCocktailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CustomCocktailService {

    private final CustomCocktailRepository customCocktailRepository;

    @Autowired
    public CustomCocktailService(CustomCocktailRepository customCocktailRepository) {
        this.customCocktailRepository = customCocktailRepository;
    }

    public CustomCocktail saveCocktail(CustomCocktail cocktail, MultipartFile imageFile, List<Ingredient> ingredients) {
        // 이미지 처리 로직
        if (imageFile != null && !imageFile.isEmpty()) {
            Image image = new Image();
            image.setImageName(imageFile.getOriginalFilename());
            image.setImagePath("/path/to/save/" + imageFile.getOriginalFilename()); // 실제로는 파일을 저장하고 경로를 설정해야 합니다.
            image.setImageUse("cocktail");
            cocktail.setImage(image, "cocktail");
        }

        // 재료 리스트 추가
        for (Ingredient ingredient : ingredients) {
            cocktail.addIngredient(ingredient);
        }

        return customCocktailRepository.save(cocktail);
    }

    public List<CustomCocktail> getAllCocktails() {
        return (List<CustomCocktail>) customCocktailRepository.findAll();
    }

}
