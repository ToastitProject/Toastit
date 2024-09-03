package alcoholboot.toastit.feature.seasonalcocktail.service;

import java.time.LocalDate;

public interface SeasonalCocktailService {

    /**
     * 현재 날짜를 반환
     *
     * @return 현재 날짜
     */
    LocalDate getCurrentDate();

    /**
     * 주어진 월에 해당하는 계절을 반환
     *
     * @param month 월 (1~12)
     * @return 해당 월의 계절 ("봄", "여름", "가을", "겨울")
     */
    String getSeasonForMonth(int month);
}