package alcoholboot.toastit.infra.email.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이메일 인증번호 확인을 위한 요청 DTO.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailCheckRequest {

    /**
     * 사용자의 이메일.
     * <p>이메일 형식을 검증합니다.</p>
     */
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Pattern(
            regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$",
            message = "올바르지 않은 이메일 형식입니다."
    )
    private String email;

    /**
     * 이메일로 전송된 인증번호.
     * <p>6자리의 영문자 또는 숫자를 검증합니다.</p>
     */
    @NotEmpty(message = "인증번호를 입력해주세요.")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{6}$",
            message = "6자리의 인증번호를 입력해주세요."
    )
    private String authCode;
}