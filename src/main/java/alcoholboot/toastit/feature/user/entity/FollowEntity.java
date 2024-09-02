package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.global.entity.JpaAuditingFields;

import jakarta.persistence.*;

import lombok.*;

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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private UserEntity followee;
}