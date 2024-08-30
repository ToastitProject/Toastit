package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.type.Authority;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Setter
    private Long id;
    @Setter
    private String email;

    @Setter
    private String nickname;

    @Setter
    private String password;

    private Authority authority;

    @Setter
    private String profileImageUrl;

    private String providerType;

    private LocalDateTime createDate;

    public void update(String nickname, String password, String profileImageUrl, String providerType) {
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.providerType = providerType;
    }

    public UserEntity convertToEntity(){
        return UserEntity.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .authority(this.authority)
                .profileImageUrl(this.profileImageUrl)
                .providerType(this.providerType)
                .build();
    }
    public LocalDate getCreateDate() {
        return this.createDate != null ? this.createDate.toLocalDate() : null;
    }
}