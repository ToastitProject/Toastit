package alcoholboot.toastit.feature.seasonalcocktail.service.impl;

import alcoholboot.toastit.feature.seasonalcocktail.service.SeasonalCocktailService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * {@link SeasonalCocktailService} SeasonalCocktailService 인터페이스의 구현체
 */
@Service
public class SeasonalCocktailServiceImpl implements SeasonalCocktailService {

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


    @Override
    public String getCocktailForSeason(String season) {
        return switch (season) {
            case "봄" -> "네그로니";
            case "여름" -> "모히토";
            case "가을" -> "올드 패션드";
            case "겨울" -> "아이리쉬 커피";
            default -> "마가리타";
        };
    }
}