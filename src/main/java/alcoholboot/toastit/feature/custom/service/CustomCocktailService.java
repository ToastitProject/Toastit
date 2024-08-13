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

    public List<CustomCocktail> getAllCocktails() {
        return (List<CustomCocktail>) customCocktailRepository.findAll();
    }

    public void saveCocktail(CustomCocktail customCocktail) {
        customCocktailRepository.save(customCocktail);
    }
}
