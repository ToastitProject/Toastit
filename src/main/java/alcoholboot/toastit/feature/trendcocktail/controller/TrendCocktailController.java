package alcoholboot.toastit.feature.trendcocktail.controller;

import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
import alcoholboot.toastit.feature.trendcocktail.dto.TrendCocktailDTO;
import alcoholboot.toastit.feature.trendcocktail.entity.TrendCocktail;
import alcoholboot.toastit.feature.trendcocktail.service.TrendCocktailService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TrendCocktailController {

    private final TrendCocktailService trendCocktailService;
    private final CocktailService cocktailService;

    /**
     * 네이버 데이터랩 통합검색 API 요청문을 전송하고 응답을 받는 엔드포인트 입니다.
     * @return : 요청에 대한 응답결과가 나와있는 페이지로 이동합니다.
     */
    @GetMapping("/baseCocktailSearch")
    public String nSearchView() {
        List<String> cocktails = cocktailService.getAllNames();
        return trendCocktailService.getSearchVolume(cocktails);
    }

    /**
     * 네이버 데이터랩 통합검색 API 응답문을 받아와, 객체로 변환하여 DB에 저장하는 기능입니다.
     * @param jsonString : API 응답 본문 입니다.
     * @return : 응답의 수신 결과를 전송합니다.
     */
    @PostMapping("/receive/trendCocktailData")
    public ResponseEntity<String> receiveData(@RequestBody String jsonString) {
        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // '||' 특수문자를 기준으로 응답을 분리
            String[] requests = jsonString.split("\\|\\|");
            List<TrendCocktailDTO> trendCocktailDTOList = new ArrayList<>();

            for (String request : requests) {
                // 각 요청을 매핑하여 객체 생성
                TrendCocktailDTO trendCocktailDTO = objectMapper.readValue(request, TrendCocktailDTO.class);
                trendCocktailDTOList.add(trendCocktailDTO);

                // 각 요청의 결과를 DB에 저장
                for (TrendCocktailDTO.Result result : trendCocktailDTO.getResults()) {
                    TrendCocktail cocktail = new TrendCocktail();

                    cocktail.setStartDate(trendCocktailDTO.getStartDate());
                    cocktail.setEndDate(trendCocktailDTO.getEndDate());

                    cocktail.setKeyword(result.getKeywords().get(0));
                    cocktail.setName(result.getKeywords().get(0));

                    List<TrendCocktailDTO.Data> dataList = result.getData();
                    if (dataList.size() > 1) {
                        cocktail.setSearchVolumeTwoMonthAgo(dataList.get(0).getRatio());
                        cocktail.setSearchVolumeOneMonthAgo(dataList.get(1).getRatio());
                    } else if (dataList.size() == 1) {
                        cocktail.setSearchVolumeTwoMonthAgo(dataList.get(0).getRatio());
                        cocktail.setSearchVolumeOneMonthAgo(0); // 기본값 설정
                    } else {
                        cocktail.setSearchVolumeTwoMonthAgo(0); // 기본값 설정
                        cocktail.setSearchVolumeOneMonthAgo(0); // 기본값 설정
                    }

                    // DB에 저장
                    trendCocktailService.save(cocktail);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("데이터 처리 중 오류가 발생했습니다.");
        }

        return ResponseEntity.ok("데이터가 성공적으로 수신되었습니다.");
    }
}
