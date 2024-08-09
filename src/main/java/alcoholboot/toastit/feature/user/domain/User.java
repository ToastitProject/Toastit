package alcoholboot.toastit.feature.user.domain;


import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.type.Authority;
import lombok.*;

import java.util.List;

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

    private List<Like> likes;

    private List<Follow> follows;

    public UserEntity convertToEntity(){
        return UserEntity.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .authority(this.authority)
                .build();
    }
}