package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.repository.UserRepository;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.infra.email.service.VerificationService;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RandomNickname randomNickname;
    private final VerificationService verificationService;

    @Value("${image.default-profile-path}")
    private String defaultProfileImg;

    @Transactional
    public void save(UserJoinRequest userJoinDto) {
        // 이메일 중복 체크
        if (findByEmail(userJoinDto.getEmail()).isPresent()) {
            throw new CustomException(CommonExceptionCode.EXIST_EMAIL_ERROR);
        }

        // 이메일 인증번호 체크
        if (!verificationService.verifyCode(userJoinDto.getEmail(), userJoinDto.getAuthCode())) {
            throw new CustomException(CommonExceptionCode.TIMEOUT_LOGOUT);
        }

        User user = userJoinDto.toDomain();

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

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntity::convertToDomain);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmailAndProviderType(String email, String providerType) {
        return userRepository.findByEmailAndProviderType(email, providerType).map(UserEntity::convertToDomain);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(UserEntity::convertToDomain);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).map(UserEntity::convertToDomain);
    }

    @Transactional
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    @Transactional
    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    /**
     * 비밀번호 암호화
     *
     * @param password 비밀번호
     * @return 암호화된 비밀번호
     */
    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}