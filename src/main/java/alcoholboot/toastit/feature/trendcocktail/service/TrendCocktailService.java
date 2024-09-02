package alcoholboot.toastit.feature.trendcocktail.service;

import alcoholboot.toastit.feature.trendcocktail.entity.TrendCocktail;

import java.util.List;

public interface TrendCocktailService {

    void save(TrendCocktail trendCocktail);

    String getSearchVolume(List<String> keywords);

    List<TrendCocktail> findTop5BySearchVolume();
}