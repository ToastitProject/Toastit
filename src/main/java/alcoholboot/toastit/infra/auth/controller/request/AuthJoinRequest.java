package alcoholboot.toastit.infra.auth.controller.request;

import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.type.Authority;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.*;

/**
 * 사용자 회원가입 요청 DTO.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthJoinRequest {

    /**
     * 사용자의 이메일.
     */
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(
            regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$",
            message = "이메일 형식으로 입력해주세요."
    )
    private String email;

    /**
     * 사용자의 비밀번호.
     */
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$",
            message = "영어, 숫자, 특수문자를 포함한 8~16자 비밀번호를 입력해주세요."
    )
    private String password;

    /**
     * 비밀번호 확인 필드.
     */
    @NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    private String passwordCheck;

    /**
     * 이메일 인증 코드.
     */
    @NotEmpty(message = "이메일을 인증해주세요.")
    private String authCode;

    /**
     * 비밀번호와 비밀번호 확인이 일치하는지 검증합니다.
     *
     * @return 비밀번호가 일치하면 true, 아니면 false
     */
    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatching() {
        return password != null && password.equals(passwordCheck);
    }

    /**
     * DTO를 User 도메인 객체로 변환합니다.
     *
     * @return User 도메인 객체
     */
    public User toDomain() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .authority(Authority.USER)
                .providerType("internal")
                .build();
    }
}