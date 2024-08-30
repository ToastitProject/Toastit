package alcoholboot.toastit.feature.calendarcocktail.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RecommendByDateService {

    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }
}
