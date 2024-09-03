package alcoholboot.toastit.auth.email.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
    void sendFormMail(String to, String subject, String body);
    String setAuthForm(String authcode);
}
