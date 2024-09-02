package alcoholboot.toastit.feature.trendcocktail.service;

import alcoholboot.toastit.feature.trendcocktail.entity.TrendCocktailEntity;
import alcoholboot.toastit.feature.trendcocktail.repository.TrendCocktailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataSaveService {
    private final TrendCocktailRepository trendCocktailRepository;

    @Transactional
    public void save(TrendCocktailEntity trendCocktailEntity) {
        trendCocktailRepository.save(trendCocktailEntity);
    }

    public void saveAll(List<TrendCocktailEntity> trendCocktailEntity){
        trendCocktailRepository.saveAll(trendCocktailEntity);
    }
}
