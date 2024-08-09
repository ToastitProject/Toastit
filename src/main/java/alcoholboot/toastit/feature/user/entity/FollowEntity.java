package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.user.domain.Follow;
import alcoholboot.toastit.global.Entity.JpaAuditingFields;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "follow")
public class FollowEntity extends JpaAuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private UserEntity follower;

    @ManyToOne
    @JoinColumn(name = "followee_id")
    private UserEntity followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private UserEntity user;

    public Follow convertToDomain(){
        return Follow.builder()
                .id(this.id)
                .followee(this.followee.convertToDomain())
                .follower(this.follower.convertToDomain())
                .build();
    }

}
