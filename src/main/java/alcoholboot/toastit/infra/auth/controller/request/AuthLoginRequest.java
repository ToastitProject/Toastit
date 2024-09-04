package alcoholboot.toastit.infra.auth.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.*;

/**
 * 사용자 로그인 요청 DTO.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthLoginRequest {

    /**
     * 사용자의 이메일 (올바른 형식 검증).
     */
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Pattern(
            regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$",
            message = "올바르지 않은 이메일 형식입니다."
    )
    private String email;

    /**
     * 사용자의 비밀번호 (8~16자, 영어, 숫자, 특수문자 포함).
     */
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$",
            message = "올바르지 않은 비밀번호 형식입니다."
    )
    private String password;
}