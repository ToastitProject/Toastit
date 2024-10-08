package alcoholboot.toastit.infra.email.util;

import java.security.SecureRandom;

/**
 * 랜덤 인증 코드를 생성하는 유틸리티 클래스.
 */
public class RandomAuthCode {

    // 인증 코드에 사용될 문자들 (영문 대문자 및 숫자)
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // 인증 코드의 길이 (6자리)
    private static final int CODE_LENGTH = 6;

    // 보안성을 위한 SecureRandom 인스턴스
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 6자리 랜덤 인증 코드를 생성합니다.
     *
     * @return 생성된 랜덤 인증 코드
     */
    public static String generate() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);

        // 지정된 길이만큼 랜덤 문자를 선택하여 인증 코드를 생성
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return code.toString(); // 생성된 인증 코드를 반환
    }
}