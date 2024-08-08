package alcoholboot.toastit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ToastitApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToastitApplication.class, args);
    }
}