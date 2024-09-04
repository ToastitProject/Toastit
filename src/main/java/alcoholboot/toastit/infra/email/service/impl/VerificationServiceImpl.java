package alcoholboot.toastit.infra.email.service.impl;

import alcoholboot.toastit.infra.email.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 인증 코드를 Redis에 저장하고 검증하는 서비스 구현체.
 */
@Service
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    // 키와 값을 모두 String 타입으로 처리하는 Redis 템플릿
    private final StringRedisTemplate redisTemplate;

    // 인증 코드 만료 시간 (10분)
    private final long VERIFICATION_CODE_EXPIRATION = 10;

    /**
     * 이메일 주소에 대한 인증 코드를 Redis에 저장합니다.
     *
     * @param email 이메일 주소
     * @param code  인증 코드
     */
    @Override
    public void saveCode(String email, String code) {
        // 이메일을 키로 사용하고, 인증 코드를 값으로 설정하여 Redis에 저장
        redisTemplate.opsForValue().set(email, code, VERIFICATION_CODE_EXPIRATION, TimeUnit.MINUTES);
    }

    /**
     * 이메일 주소에 저장된 인증 코드를 검증합니다.
     *
     * @param email 이메일 주소
     * @param code  입력된 인증 코드
     * @return 저장된 인증 코드와 입력된 코드가 일치하면 true, 그렇지 않으면 false
     */
    @Override
    public boolean verifyCode(String email, String code) {
        // Redis에서 이메일에 해당하는 인증 코드를 가져옴
        String storedCode = redisTemplate.opsForValue().get(email);
        // 입력된 인증 코드와 저장된 코드가 일치하는지 확인
        return code.equals(storedCode);
    }
}