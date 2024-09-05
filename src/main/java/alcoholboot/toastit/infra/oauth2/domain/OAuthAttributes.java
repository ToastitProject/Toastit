package alcoholboot.toastit.infra.oauth2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * OAuth2 사용자 정보를 담는 클래스.
 * 소셜 로그인 제공자(Kakao, Naver, Google)에 따른 사용자 정보를 처리합니다.
 */
@Getter
@Builder
@AllArgsConstructor
public class OAuthAttributes {

    private Map<String, Object> attributes;     // OAuth2 제공자에서 반환하는 유저 정보
    private String nameAttributesKey;           // 사용자 ID 키 (Kakao: id 등)
    private String attributeId;                 // 사용자 ID 값
    private String email;                       // 사용자 이메일
    private String nickname;                    // 사용자 닉네임
    private String profileImageUrl;             // 프로필 이미지 URL

    /**
     * 소셜 로그인 제공자에 따라 OAuthAttributes 객체를 생성합니다.
     *
     * @param socialName 소셜 로그인 제공자 이름 (kakao, naver, google)
     * @param userNameAttributeName 사용자 이름 속성 키
     * @param attributes 사용자 속성 정보
     * @return OAuthAttributes 객체
     */
    public static OAuthAttributes of(String socialName, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(socialName)) {
            return ofKakao(userNameAttributeName, attributes);
        } else if ("naver".equals(socialName)) {
            return ofNaver(userNameAttributeName, attributes);
        } else if ("google".equals(socialName)) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        return null;
    }

    /**
     * 카카오 로그인 정보 처리.
     *
     * @param userNameAttributeName 사용자 이름 속성 키
     * @param attributes 사용자 속성 정보
     * @return OAuthAttributes 객체
     */
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .nickname(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .profileImageUrl(String.valueOf(kakaoProfile.get("profile_image_url")))
                .nameAttributesKey(userNameAttributeName)
                .attributeId(String.valueOf(attributes.get(userNameAttributeName)))
                .attributes(attributes)
                .build();
    }

    /**
     * 네이버 로그인 정보 처리.
     *
     * @param userNameAttributeName 사용자 이름 속성 키
     * @param attributes 사용자 속성 정보
     * @return OAuthAttributes 객체
     */
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .nickname(String.valueOf(response.get("nickname")))
                .email(String.valueOf(response.get("email")))
                .profileImageUrl(String.valueOf(response.get("profile_image")))
                .nameAttributesKey(userNameAttributeName)
                .attributeId(String.valueOf(attributes.get(userNameAttributeName)))
                .attributes(attributes)
                .build();
    }

    /**
     * 구글 로그인 정보 처리.
     *
     * @param usernameAttributeName 사용자 이름 속성 키
     * @param attributes 사용자 속성 정보
     * @return OAuthAttributes 객체
     */
    private static OAuthAttributes ofGoogle(String usernameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nickname((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImageUrl((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributesKey(usernameAttributeName)
                .attributeId(String.valueOf(attributes.get(usernameAttributeName)))
                .attributes(attributes)
                .build();
    }
}