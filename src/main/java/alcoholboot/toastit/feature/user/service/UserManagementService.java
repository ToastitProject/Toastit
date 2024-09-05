package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.infra.auth.controller.request.AuthJoinRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.UserEntity;

import java.util.Optional;

/**
 * 사용자 관리와 관련된 비즈니스 로직을 처리하는 서비스 인터페이스.
 * 회원가입, 사용자 조회, 비밀번호 업데이트, 소셜 로그인 여부 확인 등의 기능을 제공합니다.
 */
public interface UserManagementService {

    /**
     * 회원가입 요청을 처리하여 사용자를 저장하는 메서드.
     *
     * @param userJoinDto 회원가입 요청 DTO
     */
    void save(AuthJoinRequest userJoinDto);

    /**
     * 이메일과 소셜 로그인 제공자를 기반으로 사용자를 조회하는 메서드.
     *
     * @param email 사용자 이메일
     * @param providerType 소셜 로그인 제공자 타입
     * @return 조회된 User 객체를 포함하는 Optional 객체
     */
    Optional<User> findByEmailAndProviderType(String email, String providerType);

    /**
     * 중복되지 않은 랜덤 닉네임을 생성하는 메서드.
     *
     * @return 생성된 랜덤 닉네임
     */
    String getUniqueNickname();

    /**
     * 이메일을 통해 사용자의 비밀번호를 업데이트하는 메서드.
     *
     * @param email 사용자 이메일
     * @param newPassword 새 비밀번호
     */
    void updatePassword(String email, String newPassword);

    /**
     * 이메일을 기반으로 사용자가 존재하는지 확인하는 메서드.
     *
     * @param email 사용자 이메일
     * @return 사용자가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 특정 이메일이 소셜 로그인에 사용되는지 확인하는 메서드.
     *
     * @param email 사용자 이메일
     * @return 소셜 로그인에 사용되는 이메일이면 true, 그렇지 않으면 false
     */
    boolean isSocialLoginEmail(String email);

    /**
     * 이메일을 기반으로 사용자를 조회하는 메서드.
     *
     * @param email 사용자 이메일
     * @return 조회된 User 객체를 포함하는 Optional 객체
     */
    Optional<User> findByEmail(String email);

    /**
     * ID를 통해 사용자를 조회하는 메서드.
     *
     * @param id 사용자 ID
     * @return 조회된 User 객체를 포함하는 Optional 객체
     */
    Optional<User> findById(Long id);

    /**
     * 닉네임을 통해 사용자를 조회하는 메서드.
     *
     * @param nickname 사용자 닉네임
     * @return 조회된 User 객체를 포함하는 Optional 객체
     */
    Optional<User> findByNickname(String nickname);

    /**
     * User 엔티티를 저장하는 메서드.
     *
     * @param user 저장할 User 엔티티 객체
     */
    void save(UserEntity user);

    /**
     * 이메일을 통해 사용자를 삭제하는 메서드.
     *
     * @param email 삭제할 사용자의 이메일
     */
    void deleteByEmail(String email);

    /**
     * 비밀번호를 암호화하는 메서드.
     *
     * @param password 원본 비밀번호
     * @return 암호화된 비밀번호
     */
    String encryptPassword(String password);
}