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

    /**
     * 메인 화면을 보여주는 컨트롤러 입니다
     *
     * @param sort  : 사용자가 커스텀 칵테일 레시피를 최신등록 순 혹은 좋아요 순으로 정렬하고 싶을 때 서버로 보내는 요청 값 입니다.
     * @param model : 기본 칵테일 5개 (검색량 증가 TOP 5) 를 view 로 보내는 모델 객체 입니다.
     * @return : 연결된 메인화면 view page 로 이동합니다
     */
    @GetMapping
    public String showHomePage(@RequestParam(value = "sort", defaultValue = "latest") String sort, Model model) {
        log.info("술프링 부트에 오신걸 환영합니다.");

        //검색량이 가장 많이 증가한 기본 칵테일 5개를 가져온다
        List<TrendCocktail> getTrendCocktails = trendCocktailService.findTop5BySearchVolume();
        List<Cocktail> trendCocktails = new ArrayList<>();

        //Cocktail 타입의 자료구조에 담는다
        for (int i = 0; i < getTrendCocktails.size(); i++) {
            Cocktail cocktail = cocktailService.getSingleCocktailByName(getTrendCocktails.get(i).getName());
            trendCocktails.add(cocktail);
        }
        model.addAttribute("trendCocktails", trendCocktails);


        List<CraftCocktailEntity> cocktails;
        switch (sort) {
            case "popular":
                cocktails = craftCocktailService.getTopNCocktails(5); //좋아요순
                log.debug("좋아요순 으로 5개의 칵테일을 저장");
                break;
            case "latest":
            default:
                cocktails = craftCocktailService.getLatestCocktails(5); // 최신순
                log.debug("최신등록순 으로 5개의 칵테일을 저장");
                break;
        }
        model.addAttribute("cocktails", cocktails);
        return "index";
    }
}