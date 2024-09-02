package alcoholboot.toastit.feature.trendcocktail.controller;

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

    @GetMapping("/baseCocktailSearch")
    public String nSearchView() {
        List<String> cocktails = Arrays.asList("마가리타", "모히토", "올드 패션드", "네그로니", "다이키리", "마티니",
                "맨해튼", "위스키 사워", "진 토닉", "코스모폴리탄", "롱 아일랜드 아이스티"
                , "모스코 뮬", "블러디 메리", "피나 콜라다", "마이 타이", "민트 줄렙", "섹스 온 더 비치",
                "톰 콜린스", "화이트 러시안", "김렛", "새저랙", "미모사", "사이드카",
                "프랜치 75", "팔로마", "에비에이션", "에스프레소 마티니", "쿠바 리브레",
                "라스트 워드", "아이리시 커피", "아페롤 스프리츠", "B-52", "갓파더",
                "바나나 다이키리", "브랜디 알렉산더", "카이피리냐", "샴페인 칵테일", "그래스호퍼",
                "하비 윌뱅거", "카미카제", "레몬 드롭", "러스티 네일", "스크류드라이버", "테킬라 선라이즈",
                "블랙 러시안", "블루 라군", "좀비", "애프터글로우", "앨리스 칵테일", "알로하 과일 펀치", "아펠로",
                "애플 베리 스무디", "애플 사이다 펀치", "애플 카라테", "바나나 밀크 쉐이크", "바나나 스트로베리 쉐이크",
                "바나나 스트로베리 다이키리 타입", "보라 보라", "카스티야 핫 초콜릿", "초콜릿 음료", "초콜릿 드링크",
                "초콜릿 몽키", "콜라와 드롭스", "크랜베리 펀치", "드링킹 초콜릿", "에그 크림", "에그 노그 #4",
                "건강한 에그 노그", "에그 노그 클래식", "프라페", "후르츠 쿨러"
        );

        return trendCocktailService.getSearchVolume(cocktails);
    }

    @PostMapping("/receive/trendCocktailData")
    public ResponseEntity<String> receiveData(@RequestBody String jsonString) {
        System.out.println(jsonString);
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("서버로 들어온 데이터 : " + jsonString);

        try {
            // '||' 특수문자를 기준으로 응답을 분리
            String[] requests = jsonString.split("\\|\\|");
            List<TrendCocktailDTO> trendCocktailDTOList = new ArrayList<>();

            for (String request : requests) {
                // 각 요청을 매핑하여 객체 생성
                TrendCocktailDTO trendCocktailDTO = objectMapper.readValue(request, TrendCocktailDTO.class);
                trendCocktailDTOList.add(trendCocktailDTO);
                log.info("cocktailData 의 results 리스트 크기 " + trendCocktailDTO.getResults().size());

                // 각 요청의 결과를 DB에 저장
                for (TrendCocktailDTO.Result result : trendCocktailDTO.getResults()) {
                    TrendCocktail cocktail = new TrendCocktail();

                    System.out.println("Title: " + result.getTitle());
                    cocktail.setStartDate(trendCocktailDTO.getStartDate());
                    cocktail.setEndDate(trendCocktailDTO.getEndDate());

                    System.out.println("Keywords: " + result.getKeywords());
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
