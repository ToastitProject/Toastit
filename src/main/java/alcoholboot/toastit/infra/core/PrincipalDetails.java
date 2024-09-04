package alcoholboot.toastit.infra.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 사용자 인증 및 권한 관리를 위한 클래스.
 * {@link UserDetails}와 {@link OAuth2User} 인터페이스를 구현하여,
 * Spring Security 및 OAuth2 사용자 정보를 제공한다.
 */
public class PrincipalDetails implements UserDetails, OAuth2User {

    private final String email;  // 사용자 이메일
    private final String nickname; // 사용자 닉네임
    private final String password;  // 비밀번호 (OAuth2 사용자는 사용하지 않음)
    private final List<GrantedAuthority> authorities;  // 사용자 권한 목록
    private Map<String, Object> attributes;  // OAuth2 사용자 속성

    /**
     * 일반 사용자 생성자.
     *
     * @param email 사용자 이메일
     * @param nickname 사용자 닉네임
     * @param authorities 사용자 권한 목록
     */
    public PrincipalDetails(String email, String nickname, List<GrantedAuthority> authorities) {
        this.email = email;
        this.password = "";
        this.nickname = nickname;
        this.authorities = authorities;
    }

    /**
     * OAuth2 사용자 생성자.
     *
     * @param email 사용자 이메일
     * @param nickname 사용자 닉네임
     * @param authorities 사용자 권한 목록
     * @param attributes OAuth2 사용자 속성
     */
    public PrincipalDetails(String email, String nickname, List<GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.email = email;
        this.nickname = nickname;
        this.password = "";  // OAuth2 사용자라면 비밀번호는 사용하지 않음
        this.authorities = authorities;
        this.attributes = attributes;
    }

    /**
     * 사용자의 권한 목록을 반환한다.
     *
     * @return 권한 목록
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * 사용자의 비밀번호를 반환한다.
     *
     * @return 비밀번호 (OAuth2 사용자라면 빈 문자열)
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * 사용자의 이메일을 반환한다.
     *
     * @return 사용자 이메일
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * 계정이 만료되지 않았는지 여부를 반환한다.
     *
     * @return 항상 true (만료되지 않음)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠기지 않았는지 여부를 반환한다.
     *
     * @return 항상 true (잠기지 않음)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명이 만료되지 않았는지 여부를 반환한다.
     *
     * @return 항상 true (만료되지 않음)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화되었는지 여부를 반환한다.
     *
     * @return 항상 true (활성화됨)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * OAuth2 사용자 속성을 반환한다.
     *
     * @return OAuth2 사용자 속성 맵
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * OAuth2 사용자 이름을 반환한다.
     *
     * @return 사용자 이메일 (OAuth2에서 사용자 이름 대신 사용)
     */
    @Override
    public String getName() {
        return email;
    }
}