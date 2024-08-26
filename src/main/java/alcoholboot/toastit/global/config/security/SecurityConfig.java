package alcoholboot.toastit.global.config.security;

import alcoholboot.toastit.auth.jwt.filter.TokenAuthenticationFilter;
import alcoholboot.toastit.auth.jwt.service.TokenRenewalService;
import alcoholboot.toastit.auth.jwt.util.JwtTokenizer;
import alcoholboot.toastit.auth.oauth2.handler.OAuth2AuthenticationFailureHandler;
import alcoholboot.toastit.auth.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import alcoholboot.toastit.auth.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정 클래스
 * 애플리케이션의 보안 구성을 정의
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    // JWT Util
    private final JwtTokenizer jwtTokenizer;

    // JWT TokenRenewalService
    private final TokenRenewalService tokenRenewalService;

    // OAuth2 관련 서비스와 핸들러
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    // 모든 유저 허용 페이지
    String[] allAllowPage = new String[]{
            "/", // 메인페이지

            "/error", // 예외 처리 API
            "/test/**", // 테스트 API

            "/js/**", // JS 리소스
            "/css/**", // CSS 리소스
            "/image/**", // 이미지 리소스 및 API

            "/custom/**", // 커스텀 칵테일 레시피 API
            "/cocktails/**", // 기본 칵테일 레시피 API

            "/map/**", //지도 API
            "/email/**", // 이메일 API

            "/user/mypage", // 회원 정보 API
            "/user/eidt", // 회원 정보 수정 API

            "/user/resign",  // 회원 탈퇴 API
            "/user/logout", // 로그아웃 API

            "/like/**", // 좋아요 API
            "/follow/**" //팔로우 API
    };

    // 비로그인 유저 허용 페이지
    String[] notLoggedAllowPage = new String[]{
            "/user/login", // 로그인 API
            "/user/join", // 회원가입 API
    };

    /**
     * 보안 필터 체인
     *
     * @param http 수정할 HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception HttpSecurity 구성 시 발생한 예외
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 유저별 페이지 접근 허용
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(allAllowPage).permitAll() // 모든 유저
                .requestMatchers(notLoggedAllowPage).not().authenticated() // 비로그인 유저
                .anyRequest().authenticated()
        );

        // 세션 관리 Stateless 설정(서버가 클라이언트 상태 저장x)
        http.sessionManagement(auth -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // cors 허용
        http.csrf(csrf -> csrf.disable());

        // 로그인 폼 비활성화
        http.formLogin(auth -> auth.disable());

        // http 기본 인증(헤더) 비활성화
        http.httpBasic(auth -> auth.disable());

        // JWT 필터 사용
        http.addFilterBefore(new TokenAuthenticationFilter(jwtTokenizer, tokenRenewalService), UsernamePasswordAuthenticationFilter.class);

        // OAuth2 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService)) // 사용자 정보 처리 서비스 설정
                .successHandler(oAuth2AuthenticationSuccessHandler) // 성공 핸들러 설정
                .failureHandler(oAuth2AuthenticationFailureHandler) // 실패 핸들러 설정
        );

        // SecurityFilterChain을 빌드 후 반환
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}