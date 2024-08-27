package alcoholboot.toastit.auth.oauth2.service;

import alcoholboot.toastit.auth.common.PrincipalDetails;
import alcoholboot.toastit.auth.oauth2.domain.OAuthAttributes;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.repository.UserRepository;
import alcoholboot.toastit.feature.user.service.UserService;
import alcoholboot.toastit.feature.user.type.Authority;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
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

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // OAuthAttributes: OAuth2User의 attribute를 서비스 유형에 맞게 담아줄 클래스
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, originAttributes);
        User user = saveOrUpdate(attributes, registrationId);

        List<GrantedAuthority> authorities = Collections.singletonList(Authority.USER);

        return new PrincipalDetails(user.getEmail(), user.getNickname(),authorities, originAttributes);
    }

    /**
     * 이미 존재하는 회원이라면 이름과 프로필이미지를 업데이트해줍니다.
     * 처음 가입하는 회원이라면 User 테이블을 생성합니다.
     **/
    private User saveOrUpdate(OAuthAttributes authAttributes, String providerType) {
        // 이메일과 providerType으로 사용자 조회
        Optional<User> optionalUser = userService.findByEmailAndProviderType(authAttributes.getEmail(), providerType);

        User user;

        if (optionalUser.isPresent()) {
            // 동일한 이메일과 providerType이 존재하는 경우
            user = optionalUser.get();
        } else {
            // 동일한 이메일로 다른 providerType의 유저가 존재하는지 확인
            Optional<User> existingUserWithSameEmail = userService.findByEmail(authAttributes.getEmail());

            if (existingUserWithSameEmail.isPresent()) {
                // 동일한 이메일로 다른 providerType의 유저가 이미 존재하면 예외 처리
                throw new CustomException(CommonExceptionCode.EXIST_EMAIL_ERROR);
            }

            // 새로운 사용자인 경우
            user = User.builder()
                    .email(authAttributes.getEmail())
                    .nickname(userService.getUniqueNickname())
                    .password(userService.encryptPassword(authAttributes.getAttributeId()))  // 비밀번호 저장
                    .profileImageUrl(authAttributes.getProfileImageUrl())
                    .authority(Authority.USER)
                    .providerType(providerType)  // providerType 저장
                    .build();

            userRepository.save(user.convertToEntity());
        }

        return user;  // 저장된 사용자 객체를 반환합니다
    }
}