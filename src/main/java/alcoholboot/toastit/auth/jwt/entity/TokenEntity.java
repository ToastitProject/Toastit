package alcoholboot.toastit.auth.jwt.entity;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.global.Entity.JpaAuditingFields;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity extends JpaAuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Setter
    @Column(nullable = false, length = 300)
    private String accessToken;

    @Column(nullable = false, length = 300)
    private String refreshToken;

    @Column(nullable = false)
    private String grantType;

    public Token convertToDomain() {
        return Token.builder()
                .id(this.id)
                .user(this.userEntity.convertToDomain())
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .grantType(this.grantType)
                .build();
    }
}