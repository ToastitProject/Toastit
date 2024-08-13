package alcoholboot.toastit.global.response.custom;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * @deprecated SSR 기반의 Controller 프로젝트이기에 해당 응답 코드는 아직 사용 미정입니다 :)
 */
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
        Throwable throwable = getError(webRequest);
        if (throwable != null) {
            errorAttributes.put("message", throwable.getMessage());
        }
        return errorAttributes;
    }
}

