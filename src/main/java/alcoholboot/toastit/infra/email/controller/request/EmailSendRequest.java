package alcoholboot.toastit.infra.email.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 이메일 발송 요청을 위한 DTO.
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailSendRequest {

    /**
     * 수신자 이메일 주소.
     * <p>이메일 형식을 검증합니다.</p>
     */
    @NotEmpty(message = "이메일을 입력해주세요.")
    @Pattern(
            regexp = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$",
            message = "올바르지 않은 이메일 형식입니다."
    )
    private String email;

    /**
     * 이메일 제목.
     */
    private String subject;

    /**
     * 이메일 본문 내용.
     */
    private String text;
}