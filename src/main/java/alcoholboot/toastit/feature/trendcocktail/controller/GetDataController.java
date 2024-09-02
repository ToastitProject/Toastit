package alcoholboot.toastit.feature.trendcocktail.controller;
import alcoholboot.toastit.feature.trendcocktail.service.impl.TrendCocktailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
public class GetDataController {

    private TrendCocktailServiceImpl trendCocktailService;

    @GetMapping("/getData")
    public String getData() {
        return "feature/api/getData";
    }


}
