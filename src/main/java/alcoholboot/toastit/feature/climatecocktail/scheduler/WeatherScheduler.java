package alcoholboot.toastit.feature.climatecocktail.scheduler;

import alcoholboot.toastit.feature.climatecocktail.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class WeatherScheduler {
    private final WeatherService weatherService;

    // 매일 오전 6시에 어제 날씨 데이터들을 삭제
    @Scheduled(cron = "0 0 6 * * *", zone = "Asia/Seoul")
    @Transactional
    public void deleteOldWeatherData() {

        LocalDate now = LocalDate.now();
        String basedate = now.getYear() + "";
        if (now.getMonthValue() < 10) {
            basedate += "0" + now.getMonthValue();
        } else {
            basedate += now.getMonthValue();
        }

        int yesterday = now.getDayOfMonth() - 1;

        if (yesterday < 10) {
            basedate += "0" + yesterday;
        } else {
            basedate += yesterday;
        }

        weatherService.deleteWeatherList(weatherService.getWeatherByBaseDate(basedate));
    }
}
