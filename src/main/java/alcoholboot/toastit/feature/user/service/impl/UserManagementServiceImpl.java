package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.infra.auth.controller.request.AuthJoinRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.repository.UserRepository;
import alcoholboot.toastit.infra.email.service.VerificationService;
import alcoholboot.toastit.feature.user.service.UserManagementService;
import alcoholboot.toastit.feature.user.util.RandomNickname;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;
    private final RandomNickname randomNickname;
    private final VerificationService verificationService;

    @Value("${image.default-profile-path}")
    private String defaultProfileImg;

    @Transactional
    public void save(AuthJoinRequest authJoinRequest) {
        // 이메일 중복 체크
        if (findByEmail(authJoinRequest.getEmail()).isPresent()) {
            throw new CustomException(CommonExceptionCode.EXIST_EMAIL_ERROR);
        }

        // 이메일 인증번호 체크
        if (!verificationService.verifyCode(authJoinRequest.getEmail(), authJoinRequest.getAuthCode())) {
            throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
        }

        User user = authJoinRequest.toDomain();

        // 비밀번호 암호화
        String encryptedPassword = encryptPassword(user.getPassword());

        user.update(getUniqueNickname(), encryptedPassword, defaultProfileImg, user.getProviderType());

        userRepository.save(user.convertToEntity());
    }

    /**
     * 중복되지 않은 랜덤 닉네임 생성
     *
     * @return unique random nickname
     */
    public String getUniqueNickname() {
        String nickname;
        do {
            nickname = randomNickname.generate();
        } while (findByNickname(nickname).isPresent());

        return nickname;
    }

    /**
     * 비밀번호 암호화
     *
     * @param password 비밀번호
     * @return 암호화된 비밀번호
     */
    @Override
    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * 이메일을 기반으로 사용자의 비밀번호를 업데이트하는 메서드
     */
    @Override
    public void updatePassword(String email, String newPassword) {
        // 이메일을 통해 사용자를 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER))
                .convertToDomain();

        // 새 비밀번호 암호화
        String encodedPassword = encryptPassword(newPassword);

        // 사용자 객체의 비밀번호 업데이트
        user.setPassword(encodedPassword);

        // 변경된 사용자 정보를 저장
        userRepository.save(user.convertToEntity());
    }

    // 이메일이 소셜 로그인 계정인지 확인하는 메서드
    @Override
    @Transactional(readOnly = true)
    public boolean isSocialLoginEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER))
                .convertToDomain();

        if (user != null) {
            // providerType이 "internal"이 아니면 소셜 로그인 계정으로 간주
            return !user.getProviderType().equalsIgnoreCase("internal");
        }

        return false; // 사용자 없음
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntity::convertToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmailAndProviderType(String email, String providerType) {
        return userRepository.findByEmailAndProviderType(email, providerType).map(UserEntity::convertToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(UserEntity::convertToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).map(UserEntity::convertToDomain);
    }

    @Override
    @Transactional
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }
}