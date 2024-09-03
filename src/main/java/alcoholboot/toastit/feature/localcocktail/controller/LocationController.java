package alcoholboot.toastit.feature.localcocktail.controller;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.localcocktail.controller.request.LocationDataRequest;
import alcoholboot.toastit.feature.localcocktail.service.LocationCocktailService;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 위치 기반 요청을 처리하는 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class LocationController {

    /**
     * 구글 맵 API 키
     */
    @Value("${google.maps.api.key}")
    private String mapsApiKey;

    /**
     * 구글 지오코딩 API 키
     */
    @Value("${google.geocoding.api.key}")
    private String geocodingApiKey;

    private final LocationCocktailService locationCocktailService;

    /**
     * "/map" 엔드포인트에 대한 GET 요청 처리
     *
     * @param model API 키를 뷰에 전달하기 위한 모델
     * @return 맵 페이지의 뷰 이름
     */
    @GetMapping("/map")
    public String map(Model model) {

        log.debug("/map 경로에 대한 GET 요청을 수신했습니다.");

        model.addAttribute("mapsApiKey", mapsApiKey);
        model.addAttribute("geocodingApiKey", geocodingApiKey);

        return "feature/localcocktail/cocktail-view";
    }

    /**
     * "/map" 엔드포인트에 대한 POST 요청 처리
     *
     * @param request 위치 데이터를 포함한 요청 본문
     * @return 추천 칵테일 또는 에러 메시지를 포함한 ResponseEntity
     */
    @PostMapping("/map")
    public ResponseEntity<?> receiveLocation(@RequestBody LocationDataRequest request) {

        log.debug("POST 요청 /map - 위치 정보: 도 : {}, 시 : {}", request.getProvince(), request.getCity());

        Cocktail locationCocktail = locationCocktailService.getCocktailForLocation(request.getCity(), request.getProvince());

        if (locationCocktail == null) {
            log.debug("해당 위치에 적합한 재료를 찾을 수 없습니다.");
            return ResponseEntity.status(CommonExceptionCode.LOCATION_COCKTAIL_NOT_FOUND.getCode())
                    .body(CommonExceptionCode.LOCATION_COCKTAIL_NOT_FOUND.getData());
        }

        log.debug("추천 칵테일 : {}", locationCocktail.getStrDrink());
        return ResponseEntity.ok(locationCocktail);
    }
}