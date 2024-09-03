package alcoholboot.toastit.infra.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * {@link UserDetails} 인터페이스를 구현한 클래스
 * {@link OAuth2User} 인터페이스를 구현한 클래스
 * 사용자 인증 및 권한 관리를 위한 사용자 세부 정보를 제공한다.
 */
public class PrincipalDetails implements UserDetails, OAuth2User {
    private final String email;  // 사용자 이름
    private final String nickname; // 이름
    private final String password;  // 비밀번호
    private final List<GrantedAuthority> authorities;  // 권한 목록
    private Map<String, Object> attributes;  // OAuth2 사용자 속성

    /**
     * 생성자
     *
     * @param email 이메일
     * @param nickname 닉네임
     * @param authorities 권한 목록
     */
    public PrincipalDetails(String email, String nickname, List<GrantedAuthority> authorities){
        this.email = email;
        this.password = "";
        this.nickname = nickname;
        this.authorities = authorities;
    }

    /**
     * 생성자 - OAuth2 사용자
     */
    public PrincipalDetails(String email, String nickname, List<GrantedAuthority> authorities, Map<String, Object> attributes) {
        this.email = email;
        this.nickname = nickname;
        this.password = "";  // OAuth2 사용자라면 비밀번호는 사용하지 않음
        this.authorities = authorities;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return email;
    }
}