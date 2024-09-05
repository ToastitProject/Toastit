package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.global.entity.JpaAuditingFields;

import jakarta.persistence.*;

import lombok.*;

/**
 * 팔로우 관계를 나타내는 엔티티 클래스.
 * 사용자 간의 팔로우 정보(팔로워와 팔로우 대상자)를 관리합니다.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "follows")
public class FollowEntity extends JpaAuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 팔로우 관계의 고유 ID

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private UserEntity follower;  // 팔로우를 하는 사용자

    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private UserEntity followee;  // 팔로우를 받는 사용자
}