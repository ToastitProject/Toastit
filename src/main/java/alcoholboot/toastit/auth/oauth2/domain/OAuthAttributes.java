package alcoholboot.toastit.auth.oauth2.domain;

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

    public static OAuthAttributes of(String socialName, Map<String, Object> attributes) {

        if ("kakao".equals(socialName)) {
            return ofKakao("id", attributes);
        }

        return null;
    }

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
}