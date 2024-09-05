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

/**
 * 홈 화면을 처리하는 컨트롤러.
 * 기본 칵테일과 커스텀 칵테일의 목록을 보여줍니다.
 */
@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final CocktailService cocktailService; // 기본 레시피 서비스
    private final TrendCocktailService trendCocktailService; // 검색량 증가한 기본 레시피 서비스
    private final CraftCocktailServiceImpl craftCocktailService; // 커스텀 레시피 서비스

    /**
     * 메인 화면을 보여주는 메서드.
     * 기본 칵테일 및 커스텀 칵테일 레시피를 정렬하여 뷰에 전달합니다.
     *
     * @param sort  사용자가 커스텀 칵테일 레시피를 최신순 또는 좋아요순으로 정렬할 때 사용하는 정렬 방식 (기본값: 최신순).
     * @param model 기본 칵테일 목록과 커스텀 칵테일 목록을 뷰로 전달하기 위한 모델 객체.
     * @return 메인 화면 뷰 페이지로 이동.
     */
    @GetMapping
    public String showHomePage(@RequestParam(value = "sort", defaultValue = "latest") String sort, Model model) {
        log.info("술프링 부트에 오신걸 환영합니다.");

        // 검색량이 가장 많이 증가한 기본 칵테일 5개 가져오기
        List<TrendCocktail> getTrendCocktails = trendCocktailService.findTop5BySearchVolume();
        List<Cocktail> trendCocktails = new ArrayList<>();

        // TrendCocktail을 Cocktail 객체로 변환하여 저장
        for (int i = 0; i < getTrendCocktails.size(); i++) {
            Cocktail cocktail = cocktailService.getSingleCocktailByName(getTrendCocktails.get(i).getName());
            trendCocktails.add(cocktail);
        }
        model.addAttribute("trendCocktails", trendCocktails);  // 트렌드 칵테일 목록 전달

        // 커스텀 칵테일 정렬 방식에 따라 목록 가져오기
        List<CraftCocktailEntity> cocktails;
        switch (sort) {
            case "popular":
                cocktails = craftCocktailService.getTopNCocktails(5);  // 좋아요순 정렬
                log.debug("좋아요순으로 5개의 칵테일을 저장");
                break;
            case "latest":
            default:
                cocktails = craftCocktailService.getLatestCocktails(5);  // 최신순 정렬
                log.debug("최신등록순으로 5개의 칵테일을 저장");
                break;
        }
        model.addAttribute("cocktails", cocktails);  // 커스텀 칵테일 목록 전달

        return "index";  // 메인 페이지로 이동
    }
}