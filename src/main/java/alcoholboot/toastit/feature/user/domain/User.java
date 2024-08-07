package alcoholboot.toastit.feature.user.domain;


import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.type.Authority;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;

    private String email;

    @Setter
    private String nickname;

    @Setter
    private String password;

    private Authority authority;

    public UserEntity covertToEntity(){
        return UserEntity.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .authority(this.authority)
                .build();
    }
}
