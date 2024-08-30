package alcoholboot.toastit.feature.localcocktail.service;

import alcoholboot.toastit.feature.localcocktail.repository.LocationCocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationCocktailRepository locationCocktailRepository;

    public String getIngredients (String si, String deo) {
        return locationCocktailRepository.findIngredientsBySiAndDeo(si, deo);
    }
}
