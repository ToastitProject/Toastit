package alcoholboot.toastit.infra.email.service;

/**
 * 이메일 발송 서비스 인터페이스.
 * 간단한 텍스트 이메일과 HTML 이메일을 전송하는 메서드를 정의합니다.
 */
public interface EmailService {

    /**
     * 간단한 텍스트 이메일을 전송합니다.
     *
     * @param to 수신자 이메일 주소
     * @param subject 이메일 제목
     * @param text 이메일 본문
     */
    void sendSimpleMessage(String to, String subject, String text);

    /**
     * HTML 형식의 이메일을 전송합니다.
     *
     * @param to 수신자 이메일 주소
     * @param subject 이메일 제목
     * @param body HTML 형식의 이메일 본문
     */
    void sendFormMail(String to, String subject, String body);

    /**
     * 인증 코드를 포함한 HTML 이메일 폼을 반환합니다.
     *
     * @param authcode 인증 코드
     * @return 인증 코드가 포함된 HTML 이메일 본문
     */
    String setAuthForm(String authcode);
}