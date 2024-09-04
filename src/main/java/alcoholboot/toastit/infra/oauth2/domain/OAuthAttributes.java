package alcoholboot.toastit.infra.oauth2.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuthAttributes {
    private Map<String, Object> attributes;     // OAuth2 반환하는 유저 정보
    private String nameAttributesKey;           // Kakao(id)
    private String attributeId;
    private String email;
    private String nickname;
    private String profileImageUrl;

    public static OAuthAttributes of(String socialName, String userNameAttributeName, Map<String, Object> attributes) {

        if ("kakao".equals(socialName)) {
            return ofKakao(userNameAttributeName, attributes);
        }

        else if("naver".equals(socialName)) {
            return ofNaver(userNameAttributeName, attributes);
        }

        else if("google".equals(socialName)) {
            return ofGoogle(userNameAttributeName, attributes);
        }

        return null;
    }

    /**
     * 카카오 로그인
     * @param userNameAttributeName
     * @param attributes
     * @return
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
     * 네이버 로그인
     * @param userNameAttributeName
     * @param attributes
     * @return
     */
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");

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
     * 구글 로그인
     * @param usernameAttributeName
     * @param attributes
     * @return
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