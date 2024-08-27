package alcoholboot.toastit.feature.recommendByLocation.Service;

import alcoholboot.toastit.feature.recommendByLocation.Repository.RecommendByLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendByLocationService {
    private final RecommendByLocationRepository recommendByLocationRepository;

    public String getIngredients (String si, String deo) {
        return recommendByLocationRepository.findIngredientsBySiAndDeo(si, deo);
    }
}
