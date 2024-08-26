package alcoholboot.toastit.auth.oauth2.service;

import alcoholboot.toastit.auth.jwt.info.CustomUserDetails;
import alcoholboot.toastit.auth.oauth2.domain.OAuthAttributes;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.repository.UserRepository;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.feature.user.type.Authority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);  // OAuth2

        Map<String, Object> originAttributes = oAuth2User.getAttributes();  // OAuth2User -> attributes

        // OAuth2 서비스 id (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();    // 소셜 정보를 가져옵니다.

        // OAuthAttributes: OAuth2User의 attribute를 서비스 유형에 맞게 담아줄 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, originAttributes);
        User user = saveOrUpdate(attributes);

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(Authority.USER);

        return new CustomUserDetails(user.getEmail(), user.getNickname(), (List<GrantedAuthority>) authorities, originAttributes);
    }

    /**
     * 이미 존재하는 회원이라면 이름과 프로필이미지를 업데이트해줍니다.
     * 처음 가입하는 회원이라면 User 테이블을 생성합니다.
     **/
    private User saveOrUpdate(OAuthAttributes authAttributes) {
        Optional<User> optionalUser = userService.findByEmail(authAttributes.getEmail());
        String encryptedPassword = userService.encryptPassword(authAttributes.getAttributeId());

        User user;

        if (optionalUser.isEmpty()) {
            user = User.builder()
                    .email(authAttributes.getEmail())
                    .nickname(userService.getUniqueNickname())
                    .password(encryptedPassword)
                    .profileImageUrl(authAttributes.getProfileImageUrl())
                    .authority(Authority.USER)
                    .build();
        } else {
            user = optionalUser.get();
        }

        userRepository.save(user.convertToEntity());  // 새로운 사용자이든 업데이트된 사용자이든 저장합니다

        return user;  // 저장된 사용자 객체를 반환합니다
    }
}