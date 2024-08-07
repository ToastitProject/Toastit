package alcoholboot.toastit.auth.jwt.entity;

import alcoholboot.toastit.auth.jwt.domain.Token;
import alcoholboot.toastit.global.Entity.AuditingFields;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @Setter
    @Column(nullable = false, length = 300)
    private String accessToken;

    @Column(nullable = false, length = 300)
    private String refreshToken;

    @Column(nullable = false)
    private String grantType;

    public Token covertToDomain() {
        return Token.builder()
                .id(this.id)
                .user(this.userEntity.covertToDomain())
                .accessToken(this.accessToken)
                .refreshToken(this.refreshToken)
                .grantType(this.grantType)
                .build();
    }
}