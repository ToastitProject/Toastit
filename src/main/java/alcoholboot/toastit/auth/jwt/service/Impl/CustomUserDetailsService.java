package alcoholboot.toastit.auth.jwt.service.Impl;

import alcoholboot.toastit.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * {@link UserDetailsService} 인터페이스 구현 클래스
 * 주어진 사용자 이름을 기반으로 사용자 세부 정보를 로드하는 기능을 제공한다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    // 회원 저장소
    private final UserRepository userRepository;

    /**
     * 주어진 사용자 이름을 기반으로 사용자 세부 정보를 로드한다.
     *
     * @param username 인증을 위한 사용자 이름 (주로 이메일 또는 사용자 ID)
     * @return 사용자 세부 정보를 담고 있는 UserDetails 객체
     * @throws UsernameNotFoundException 주어진 사용자 이름에 해당하는 사용자가 없는 경우 발생
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 여기에 실제 사용자 검색 및 UserDetails 반환 로직이 추가되어야 한다.
        return null;
    }
}