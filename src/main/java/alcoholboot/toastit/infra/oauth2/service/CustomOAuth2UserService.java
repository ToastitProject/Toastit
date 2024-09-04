package alcoholboot.toastit.infra.oauth2.service;

import alcoholboot.toastit.infra.core.PrincipalDetails;
import alcoholboot.toastit.infra.oauth2.domain.OAuthAttributes;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.repository.UserRepository;
import alcoholboot.toastit.feature.user.service.UserManagementService;
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

/**
 * OAuth2 로그인 처리를 위한 서비스 구현체.
 * 사용자 정보를 불러오고, 회원가입 또는 사용자 업데이트를 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserManagementService userManagementService;
    private final UserRepository userRepository;

    /**
     * OAuth2 로그인 요청이 들어올 때, OAuth2 사용자 정보를 불러옵니다.
     *
     * @param userRequest OAuth2UserRequest 객체
     * @return OAuth2User 객체
     * @throws OAuth2AuthenticationException 인증 실패 시 예외 발생
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = service.loadUser(userRequest);  // OAuth2 사용자 정보 로드

        Map<String, Object> originAttributes = oAuth2User.getAttributes();  // 사용자 속성 정보

        // OAuth2 서비스 id (google, kakao, naver)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();  // 소셜 제공자 정보

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 사용자 속성 정보 매핑
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, originAttributes);
        User user = saveOrUpdate(attributes, registrationId);  // 사용자 정보 저장 또는 업데이트

        List<GrantedAuthority> authorities = Collections.singletonList(Authority.USER);

        // PrincipalDetails 객체를 반환하여 Spring Security에 전달
        return new PrincipalDetails(user.getEmail(), user.getNickname(), authorities, originAttributes);
    }

    /**
     * 이미 존재하는 사용자는 정보를 업데이트하고, 새로운 사용자는 회원가입 처리합니다.
     *
     * @param authAttributes OAuth 사용자 속성 정보
     * @param providerType OAuth2 제공자 타입 (google, kakao, naver 등)
     * @return 저장된 사용자 객체
     */
    private User saveOrUpdate(OAuthAttributes authAttributes, String providerType) {
        // 이메일과 providerType으로 사용자 조회
        Optional<User> optionalUser = userManagementService.findByEmailAndProviderType(authAttributes.getEmail(), providerType);

        User user;

        if (optionalUser.isPresent()) {
            // 동일한 이메일과 providerType이 존재하는 경우
            user = optionalUser.get();
        } else {
            // 동일한 이메일로 다른 providerType의 유저가 존재하는지 확인
            Optional<User> existingUserWithSameEmail = userManagementService.findByEmail(authAttributes.getEmail());

            if (existingUserWithSameEmail.isPresent()) {
                // 동일한 이메일로 다른 providerType의 유저가 이미 존재하면 예외 처리
                throw new CustomException(CommonExceptionCode.EXIST_EMAIL_ERROR);
            }

            // 새로운 사용자 생성
            user = User.builder()
                    .email(authAttributes.getEmail())
                    .nickname(userManagementService.getUniqueNickname())
                    .password(userManagementService.encryptPassword(authAttributes.getAttributeId()))  // 비밀번호 저장
                    .profileImageUrl(authAttributes.getProfileImageUrl())
                    .authority(Authority.USER)
                    .providerType(providerType)  // 소셜 제공자 타입
                    .build();

            // 새로운 사용자 저장
            userRepository.save(user.convertToEntity());
        }

        return user;  // 저장된 사용자 반환
    }
}