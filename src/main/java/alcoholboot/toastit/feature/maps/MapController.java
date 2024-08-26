package alcoholboot.toastit.feature.maps;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class MapController {

    @Value("${google.maps.api.key}")
    private String mapsApiKey;

    @Value("${google.geocoding.api.key}")
    private String geocodingApiKey;


    @GetMapping("/map")
    public String map(Model model) {
        log.info("map 으로 GetMapping 이 들어옴");
        model.addAttribute("mapsApiKey", mapsApiKey);
        model.addAttribute("geocodingApiKey", geocodingApiKey);
//        log.info("모델에 담아 보내는 MAP API KEY : "+ mapsApiKey);
//        log.info("모델에 담아 보내는 Geocoding API KEY : "+geocodingApiKey);
        return "feature/maps/main";
    }
}
