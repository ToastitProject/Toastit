package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.type.Authority;
import alcoholboot.toastit.global.Entity.JpaAuditingFields;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends JpaAuditingFields {
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(nullable = false, unique = true)
    private String nickname;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Column(nullable = false)
    private String profileImageUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CustomCocktail> cocktails = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

//    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
//    private List<LikeEntity> likes = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<FollowEntity> following = new ArrayList<>();

    @Setter
    @OneToMany(mappedBy = "followee", cascade = CascadeType.ALL)
    private List<FollowEntity> followers = new ArrayList<>();


    public UserEntity(String email, String nickname, String password, Authority authority) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.authority = authority;
    }

    public User convertToDomain(){
        return User.builder()
                .id(this.id)
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .authority(this.authority)
                .profileImageUrl(this.profileImageUrl)
                .createDate(this.createDate)
                .build();
    }
}