package alcoholboot.toastit.feature.user.entity;

import alcoholboot.toastit.feature.user.domain.Follow;
import alcoholboot.toastit.global.entity.JpaAuditingFields;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "follow")
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


    public Follow convertToDomain() {
        return Follow.builder()
                .id(this.id)
                .followerId(this.follower.getId())
                .followeeId(this.followee.getId())
                .build();
    }
}
