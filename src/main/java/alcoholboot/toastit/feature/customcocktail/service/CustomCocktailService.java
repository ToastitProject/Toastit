package alcoholboot.toastit.feature.customcocktail.service;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.customcocktail.repository.CustomCocktailRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}

