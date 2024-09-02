package alcoholboot.toastit.feature.dataSearchVolume.controller;
import alcoholboot.toastit.feature.dataSearchVolume.service.DataSaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Slf4j
@Controller
@RequiredArgsConstructor
public class GetDataController {

    private DataSaveService dataSaveService;

    @GetMapping("/getData")
    public String getData() {
        return "feature/api/getData";
    }


}
