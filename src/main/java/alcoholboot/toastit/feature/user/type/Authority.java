package alcoholboot.toastit.feature.user.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;

/**
 * 사용자 권한을 정의하는 열거형 클래스.
 * Spring Security의 GrantedAuthority 인터페이스를 구현하여 권한 정보를 제공합니다.
 */
@Getter
@AllArgsConstructor
public enum Authority implements GrantedAuthority {
    ADMIN,  // 관리자 권한
    USER;   // 일반 사용자 권한

    /**
     * 권한의 이름을 반환하는 메서드.
     * Spring Security에서 사용자의 권한을 String 형태로 반환합니다.
     *
     * @return 권한 이름 (예: "ADMIN", "USER")
     */
    @Override
    public String getAuthority() {
        return name();
    }
}