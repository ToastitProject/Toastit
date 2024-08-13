package alcoholboot.toastit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping
    public String showHomePage() {
        log.info("술프링 부트에 오신걸 환영합니다.");
        return "index";
    }
}