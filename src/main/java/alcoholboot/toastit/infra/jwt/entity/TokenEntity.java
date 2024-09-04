package alcoholboot.toastit.infra.jwt.entity;

import alcoholboot.toastit.infra.jwt.domain.Token;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.global.entity.*;

import jakarta.persistence.*;
import lombok.*;

/**
 * 데이터베이스의 "tokens" 테이블에 매핑되는 Token 엔티티를 나타냅니다.
 * 이 엔티티는 사용자와 연관되어 있으며, 액세스 토큰과 리프레시 토큰을 저장합니다.
 */
@Builder
@Getter
@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity extends JpaAuditingFields {

    /**
     * 토큰 엔티티의 고유 식별자
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 토큰 엔티티와 연관된 사용자를 일대일 관계로 설정하며,
     * LAZY 로딩 전략이 적용되어 토큰 엔티티가 로드될 때 사용자 엔티티는 즉시 로드되지 않는다.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    /**
     * 사용자의 액세스 토큰이며, 값은 필수이고, 최대 길이는 500자이다.
     */
    @Setter
    @Column(nullable = false, length = 500)
    private String accessToken;

    /**
     * 사용자의 리프레시 토큰이며, 이 필드는 필수이고, 최대 길이는 500자이다.
     */
    @Column(nullable = false, length = 500)
    private String refreshToken;

    /**
     * 토큰 인증 유형이며, 이 필드는 필수이다.
     */
    @Column(nullable = false)
    private String grantType;

    /**
     * 이 엔티티를 도메인 객체 (Token)로 변환한다.
     *
     * @return 이 엔티티를 기반으로 생성된 Token 도메인 객체.
     */
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