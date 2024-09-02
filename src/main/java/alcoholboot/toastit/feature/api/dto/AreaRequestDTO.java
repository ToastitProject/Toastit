package alcoholboot.toastit.feature.api.dto;

import lombok.Data;

@Data
public class AreaRequestDTO {
    private String areacode;
    private String step1;
    private String step2;
    private String step3;
    private String baseDate;
    private String baseTime;
    private String nx;
    private String ny;
}
