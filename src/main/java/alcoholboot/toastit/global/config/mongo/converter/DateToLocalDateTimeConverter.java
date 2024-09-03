package alcoholboot.toastit.global.config.mongo.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

    @Override
    public LocalDateTime convert(Date source) {
        return source.toInstant()
                .atZone(ZoneOffset.UTC)
                .withZoneSameInstant(ZoneOffset.ofHours(9))
                .toLocalDateTime();
    }
}