package alcoholboot.toastit.feature.api.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("AreaRequestDTO")
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
