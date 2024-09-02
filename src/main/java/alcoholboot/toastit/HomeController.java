package alcoholboot.toastit;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.service.impl.CraftCocktailServiceImpl;
import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
import alcoholboot.toastit.feature.trendcocktail.entity.TrendCocktail;
import alcoholboot.toastit.feature.trendcocktail.service.TrendCocktailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final CocktailService cocktailService; //기본 레시피
    private final TrendCocktailService trendCocktailService; // 기본 레시피의 검색량
    private final CraftCocktailServiceImpl craftCocktailService; //커스텀 레시피

    @GetMapping
    public String showHomePage(@RequestParam(value = "sort",defaultValue = "latest") String sort, Model model) {
        log.info("술프링 부트에 오신걸 환영합니다.");

        List<TrendCocktail> getTrendCocktails = trendCocktailService.findTop5BySearchVolume();
        log.info("트렌드 칵테일 리스트의 크기 : "+getTrendCocktails.size());
        List<Cocktail> trendCocktails = new ArrayList<>();

        for (int i = 0; i < getTrendCocktails.size(); i++) {
            Cocktail cocktail = cocktailService.getSingleCocktailByName(getTrendCocktails.get(i).getName());
            trendCocktails.add(cocktail);
            log.info("검색량 " + i + " 번 째 칵테일 : " + cocktail);
        }
        model.addAttribute("trendCocktails", trendCocktails);

        List<CraftCocktailEntity> cocktails;
        log.info("화면에서 보내준 sort 값 : "+sort);
        switch (sort) {
            case "popular":
                cocktails = craftCocktailService.getTopNCocktails(5); //좋아요순
                log.info("좋아요 순 정렬 요청 보냄");
                break;
            case "latest":
            default:
                cocktails = craftCocktailService.getLatestCocktails(5); // 최신순
                log.info("최신순 정렬 요청 보냄");
                break;
        }
        model.addAttribute("cocktails", cocktails);



        return "index";
    }
}