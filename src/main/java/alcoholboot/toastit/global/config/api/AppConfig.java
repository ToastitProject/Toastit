package alcoholboot.toastit.global.config.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 애플리케이션 전역 설정 클래스.
 * 주로 RestTemplate 빈을 등록합니다.
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate 빈을 생성합니다.
     * RestTemplate은 HTTP 요청을 수행하기 위한 스프링의 유틸리티 클래스입니다.
     *
     * @return RestTemplate 객체
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}