package alcoholboot.toastit.feature.seasonalcocktail.service.impl;

import alcoholboot.toastit.feature.seasonalcocktail.service.SeasonalCocktailService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * {@link SeasonalCocktailService} SeasonalCocktailService 인터페이스의 구현체
 */
@Service
public class SeasonalCocktailServiceImpl implements SeasonalCocktailService {

    private final Random random = new Random();

    /**
     * 현재 날짜를 반환
     *
     * @return 현재 날짜
     */
    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    /**
     * 주어진 월에 해당하는 계절을 반환
     *
     * @param month 월 (1~12)
     * @return 해당 월의 계절 ("봄", "여름", "가을", "겨울")
     */
    @Override
    public String getSeasonForMonth(int month) {
        if (month >= 3 && month <= 5) {
            return "봄";
        } else if (month >= 6 && month <= 8) {
            return "여름";
        } else if (month >= 9 && month <= 11) {
            return "가을";
        } else {
            return "겨울";
        }
    }



    public String getCocktailForSeason(String season) {
        return switch (season) {
            case "봄" -> {
                List<String> springCocktails = List.of("애플 사이다 펀치", "애플 카라테", "아펠로", "블랙 러시안", "미모사", "사이드카", "프랜치 75");
                yield springCocktails.get(random.nextInt(springCocktails.size()));
            }
            case "여름" -> {
                List<String> summerCocktails = List.of("후르츠 쿨러", "바나나 스트로베리", "바나나 스트로베리 쉐이크 다이키리 타입", "바나나 밀크 쉐이크", "알로하 과일 펀치", "블루 라군", "라스트 워드", "카이피리냐");
                yield summerCocktails.get(random.nextInt(summerCocktails.size()));
            }
            case "가을" -> {
                List<String> fallCocktails = List.of("올드 패션드", "네그로니", "코스모폴리탄", "블러디 메리", "새저랙", "아페롤 스프리츠", "좀비", "애프터글로우", "보라 보라", "크랜베리 펀치", "샴페인 칵테일", "김렛");
                yield fallCocktails.get(random.nextInt(fallCocktails.size()));
            }
            case "겨울" -> {
                List<String> winterCocktails = List.of("프라페", "에그 노그", "건강한 에그 노그", "에그 노그 클래식", "초콜릿 드링크", "초콜릿 몽키", "초콜릿 음료", "카스티야 핫 초콜릿", "에비에이션", "에스프레소 마티니", "아이리쉬 커피");
                yield winterCocktails.get(random.nextInt(winterCocktails.size()));
            }
            default -> "마가리타";
        };
    }
}