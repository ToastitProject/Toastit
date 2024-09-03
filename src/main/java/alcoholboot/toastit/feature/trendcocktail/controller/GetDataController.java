package alcoholboot.toastit.feature.trendcocktail.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GetDataController {

    @GetMapping("/getTrendCocktailData")
    public String getTrendCocktailData() {
        return "trendcocktail/search-data";
    }
}