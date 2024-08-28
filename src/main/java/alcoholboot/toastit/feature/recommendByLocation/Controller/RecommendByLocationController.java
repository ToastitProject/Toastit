package alcoholboot.toastit.feature.recommendByLocation.Controller;

import alcoholboot.toastit.feature.defaultcocktail.domain.Cocktail;
import alcoholboot.toastit.feature.defaultcocktail.service.CocktailService;
import alcoholboot.toastit.feature.recommendByLocation.Service.RecommendByLocationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


@Controller
@Slf4j
@RequiredArgsConstructor
public class RecommendByLocationController {
    private final RecommendByLocationService recommendByLocationService;
    private final CocktailService cocktailService;

    @Value("${google.maps.api.key}")
    private String mapsApiKey;

    @Value("${google.geocoding.api.key}")
    private String geocodingApiKey;


    @GetMapping("/map")
    public String map(Model model) {
        log.info("map 으로 GetMapping 이 들어옴");
        model.addAttribute("mapsApiKey", mapsApiKey);
        model.addAttribute("geocodingApiKey", geocodingApiKey);
//        log.info("모델에 담아 보내는 MAP API KEY : "+ mapsApiKey);
//        log.info("모델에 담아 보내는 Geocoding API KEY : "+geocodingApiKey);
        return "/feature/maps/main";
    }

    @PostMapping("/map")
    public ResponseEntity<?> receiveLocation(@RequestBody LocationData locationData) {
        log.info("postMapping 요청");
        log.info("사용자로 부터 받은 지역 : " + locationData.getProvince());
        log.info("사용자로 부터 받은 시 : " + locationData.getCity());

        String deo = locationData.getProvince();
        String si = locationData.getCity();

        log.info("쿼리문을 돌릴 문자열 광역시/도 : "+deo);
        log.info("쿼리문을 돌릴 문자열 시 : "+si);

        // **시(도) **시(군) 에서 저장된 재료 4개를 List 로 불러온다
        String ingredientsString = recommendByLocationService.getIngredients(si, deo);
        List<String> ingredients = Arrays.asList(ingredientsString.split(","));

        // 재료가 비어 있는지 확인
        if (ingredients.isEmpty()) {
            log.warn("저장된 재료가 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("저장된 재료가 없습니다.");
        } else {
            for (String ingredient : ingredients) {
                log.info("저장된 재료들 : " + ingredient);
            }
            Random random = new Random();
            Cocktail recommendedCocktail = null;

            // 칵테일을 찾을 때까지 반복
            while (recommendedCocktail == null) {
                String randomIngredient = ingredients.get(random.nextInt(ingredients.size()));
                log.info("랜덤으로 선택된 재료 : " + randomIngredient);

                // 랜덤 재료로 뽑힌 칵테일 중 하나를 랜덤으로 뽑는다.
                List<Cocktail> recommendedCocktails = cocktailService.getCocktailsByIngredient(randomIngredient);

                // 추천된 칵테일이 비어 있는지 확인
                if (!recommendedCocktails.isEmpty()) {
                    recommendedCocktail = recommendedCocktails.get(random.nextInt(recommendedCocktails.size()));
                    log.info("랜덤으로 뽑힌 칵테일 : " + recommendedCocktail.getStrDrink());
                } else {
                    log.warn("추천된 칵테일이 없습니다. 다른 재료를 선택합니다.");
                }
            }
            log.info("view 로 추천 칵테일 하나를 보내준다 : " + recommendedCocktail.getStrDrink());
            return ResponseEntity.ok(recommendedCocktail);
        }
    }
    @Getter
    public static class LocationData {
        private String province;
        private String city;
    }
}

