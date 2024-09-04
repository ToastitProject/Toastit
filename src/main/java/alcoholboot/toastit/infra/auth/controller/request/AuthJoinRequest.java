package alcoholboot.toastit.infra.auth.controller.request;

import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.type.Authority;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthJoinRequest {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$",
            message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,16}$",
            message = "영어, 숫자, 특수문자를 포함한 8~16자 비밀번호를 입력해주세요.")
    private String password;

    @NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    private String passwordCheck;

    @NotEmpty(message = "이메일을 인증해주세요.")
    private String authCode;

    @AssertTrue(message = "비밀번호와 비밀번호 확인이 일치하지 않습니다.")
    public boolean isPasswordMatching() {
        if (password == null || passwordCheck == null) {
            return true;
        }
        return password.equals(passwordCheck);
    }

    /**
     * entity 변환
     *
     * @return User Domain
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