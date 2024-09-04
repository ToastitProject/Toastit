package alcoholboot.toastit.feature.seasonalcocktail.controller;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
import alcoholboot.toastit.feature.seasonalcocktail.service.SeasonalCocktailService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * 계절별 칵테일을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/season")
@RequiredArgsConstructor
public class SeasonalCocktailController {

    private final SeasonalCocktailService seasonalCocktailService;
    private final CocktailService cocktailService;

//    /**
//     * 랜덤 칵테일과 현재 날짜 및 계절 정보를 가져와서 모델에 추가
//     *
//     * @param count 가져올 칵테일의 개수 (기본값 1)
//     * @param model 뷰에 전달할 데이터 모델
//     * @return 뷰 이름
//     */
//    @GetMapping
//    public String getRandomAndDate(
//            @RequestParam(defaultValue = "1") int count,
//            Model model
//    ) {
//        List<Cocktail> cocktails = cocktailService.getRandomCocktails(count);
//        LocalDate currentDate = seasonalCocktailService.getCurrentDate();
//        String season = seasonalCocktailService.getSeasonForMonth(currentDate.getMonthValue());
//
//        model.addAttribute("cocktails", cocktails);
//        model.addAttribute("currentDate", currentDate);
//        model.addAttribute("season", season);
//
//        return "feature/seasonalcocktail/recipe-preview";
//    }

    @GetMapping
    public String getSeasonalCocktail(Model model) {
        LocalDate currentDate = seasonalCocktailService.getCurrentDate();
        String season = seasonalCocktailService.getSeasonForMonth(currentDate.getMonthValue());
        String cocktailName = seasonalCocktailService.getCocktailForSeason(season);

        Cocktail cocktail = cocktailService.getSingleCocktailByName(cocktailName);

        model.addAttribute("cocktail", cocktail);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("season", season);

        return "feature/seasonalcocktail/recipe-preview";
    }
}