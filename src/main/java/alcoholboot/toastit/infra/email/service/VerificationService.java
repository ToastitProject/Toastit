package alcoholboot.toastit.infra.email.service;

/**
 * 이메일 인증 코드를 저장하고 검증하는 서비스 인터페이스.
 */
public interface VerificationService {

    /**
     * 이메일 주소에 대한 인증 코드를 저장합니다.
     *
     * @param email 이메일 주소
     * @param code  저장할 인증 코드
     */
    void saveCode(String email, String code);

    /**
     * 저장된 인증 코드와 입력된 인증 코드를 비교하여 검증합니다.
     *
     * @param email 이메일 주소
     * @param code  입력된 인증 코드
     * @return 인증 코드가 일치하면 true, 그렇지 않으면 false
     */
    boolean verifyCode(String email, String code);
}