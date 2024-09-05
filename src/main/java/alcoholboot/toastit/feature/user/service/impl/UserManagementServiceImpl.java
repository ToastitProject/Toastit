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

/**
 * 사용자 관리와 관련된 비즈니스 로직을 처리하는 서비스 구현 클래스.
 * 사용자 회원가입, 비밀번호 암호화, 사용자 정보 조회, 삭제 등을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepository;
    private final RandomNickname randomNickname;
    private final VerificationService verificationService;

    @Value("${image.default-profile-path}")
    private String defaultProfileImg;

    /**
     * 새로운 사용자를 DB에 저장하는 메서드.
     * 이메일 중복 체크와 이메일 인증을 검증 후 사용자 정보를 저장합니다.
     *
     * @param authJoinRequest 사용자 회원가입 요청 DTO
     */
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
     * 중복되지 않은 랜덤 닉네임을 생성하는 메서드.
     *
     * @return 중복되지 않은 랜덤 닉네임
     */
    public String getUniqueNickname() {
        String nickname;
        do {
            nickname = randomNickname.generate();
        } while (findByNickname(nickname).isPresent());

        return nickname;
    }

    /**
     * 비밀번호를 암호화하는 메서드.
     *
     * @param password 원본 비밀번호
     * @return 암호화된 비밀번호
     */
    @Override
    public String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * 이메일을 기반으로 사용자의 비밀번호를 업데이트하는 메서드.
     *
     * @param email 사용자 이메일
     * @param newPassword 새 비밀번호
     */
    @Override
    public void updatePassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER))
                .convertToDomain();

        String encodedPassword = encryptPassword(newPassword);
        user.setPassword(encodedPassword);

        userRepository.save(user.convertToEntity());
    }

    /**
     * 해당 이메일이 소셜 로그인에 이용되는 이메일인지 확인하는 메서드.
     *
     * @param email 이메일 주소
     * @return 소셜 로그인 이메일이면 true, 아니면 false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isSocialLoginEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER))
                .convertToDomain();

        return !user.getProviderType().equalsIgnoreCase("internal");
    }

    /**
     * 특정 이메일이 DB에 존재하는지 확인하는 메서드.
     *
     * @param email 이메일 주소
     * @return 존재하면 true, 없으면 false
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * 이메일을 통해 사용자를 조회하는 메서드.
     *
     * @param email 사용자 이메일
     * @return 사용자 객체를 포함하는 Optional 객체
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserEntity::convertToDomain);
    }

    /**
     * 이메일과 소셜 로그인 제공자를 통해 사용자를 조회하는 메서드.
     *
     * @param email 사용자 이메일
     * @param providerType 소셜 로그인 제공자 타입
     * @return 사용자 객체를 포함하는 Optional 객체
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmailAndProviderType(String email, String providerType) {
        return userRepository.findByEmailAndProviderType(email, providerType).map(UserEntity::convertToDomain);
    }

    /**
     * ID를 통해 사용자를 조회하는 메서드.
     *
     * @param id 사용자 ID
     * @return 사용자 객체를 포함하는 Optional 객체
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id).map(UserEntity::convertToDomain);
    }

    /**
     * 닉네임을 통해 사용자를 조회하는 메서드.
     *
     * @param nickname 사용자 닉네임
     * @return 사용자 객체를 포함하는 Optional 객체
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).map(UserEntity::convertToDomain);
    }

    /**
     * 사용자를 DB에 저장하는 메서드.
     *
     * @param user 저장할 사용자 엔티티
     */
    @Override
    @Transactional
    public void save(UserEntity user) {
        userRepository.save(user);
    }

    /**
     * 이메일을 통해 사용자를 DB에서 삭제하는 메서드.
     *
     * @param email 삭제할 사용자의 이메일
     */
    @Override
    @Transactional
    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }
}