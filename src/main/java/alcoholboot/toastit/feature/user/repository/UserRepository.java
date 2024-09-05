package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자와 관련된 데이터베이스 작업을 처리하는 리포지토리 인터페이스.
 * 이메일, 닉네임을 기반으로 사용자 정보를 조회하거나 삭제하는 메서드를 제공합니다.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * 이메일을 기반으로 사용자를 조회하는 메서드.
     *
     * @param email 사용자 이메일
     * @return 조회된 UserEntity 객체를 포함하는 Optional 객체
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * 닉네임을 기반으로 사용자를 조회하는 메서드.
     *
     * @param nickname 사용자 닉네임
     * @return 조회된 UserEntity 객체를 포함하는 Optional 객체
     */
    Optional<UserEntity> findByNickname(String nickname);

    /**
     * 이메일과 소셜 로그인 제공자를 기반으로 사용자를 조회하는 메서드.
     *
     * @param email 사용자 이메일
     * @param providerType 소셜 로그인 제공자 타입
     * @return 조회된 UserEntity 객체를 포함하는 Optional 객체
     */
    Optional<UserEntity> findByEmailAndProviderType(String email, String providerType);

    /**
     * 이메일을 기반으로 사용자가 존재하는지 확인하는 메서드.
     *
     * @param email 사용자 이메일
     * @return 사용자가 존재하면 true, 그렇지 않으면 false
     */
    boolean existsByEmail(String email);

    /**
     * 이메일을 기반으로 사용자를 삭제하는 메서드.
     *
     * @param email 사용자 이메일
     */
    void deleteByEmail(String email);
}