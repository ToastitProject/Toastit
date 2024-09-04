package alcoholboot.toastit.feature.trendcocktail.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GetDataController {

    /**
     * 네이버 데이터렙 통합검색량 API를 서버로 전송할 수 있는 페이지 입니다
     * @return : 뷰 페이지로 이동합니다.
     */
    @GetMapping("/getTrendCocktailData")
    public String getTrendCocktailData() {
        return "trendcocktail/search-data";
    }
}