package alcoholboot.toastit.feature.recommendByDate.controller;

import alcoholboot.toastit.feature.defaultcocktail.domain.Cocktail;
import alcoholboot.toastit.feature.defaultcocktail.service.CocktailService;
import alcoholboot.toastit.feature.recommendByDate.service.RecommendByDateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RecommendByDateController {

    private final RecommendByDateService recommendByDateService;
    private final CocktailService cocktailService;

    @GetMapping("/date")
    public String getRandomAndDate(
            @RequestParam(defaultValue = "1") int count,
            Model model
    ) {
        List<Cocktail> cocktails = cocktailService.getRandomCocktails(count);

        LocalDate currentDate = recommendByDateService.getCurrentDate();
        String season = getSeason(currentDate.getMonthValue());

        model.addAttribute("cocktails", cocktails);
        model.addAttribute("currentDate", currentDate);
        model.addAttribute("season", season);

        return "feature/date/main";
    }



    private String getSeason(int month) {
        if (month >= 3 && month <= 5) {
            return "봄";
        } else if (month >= 6 && month <= 8) {
            return "여름";
        } else if (month >= 9 && month <= 11) {
            return "가을";
        } else {
            return "겨울";
        }
    }
}
