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
        log.info("리다이렉션 성공!!!");
        return "index";
    }
}