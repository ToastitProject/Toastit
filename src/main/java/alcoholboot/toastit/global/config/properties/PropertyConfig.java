package alcoholboot.toastit.global.config.properties;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * 애플리케이션 속성 파일을 로드하는 설정 클래스.
 * env.properties 파일을 애플리케이션의 속성으로 등록합니다.
 */
@Configuration
@PropertySources({
        @PropertySource("classpath:properties/env.properties")
})
public class PropertyConfig {
}