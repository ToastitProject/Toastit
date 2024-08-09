package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.global.Entity.JpaAuditingFields;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Follow extends JpaAuditingFields {

    private Long id;
    private User follower;
    private User followee;
    private User user;

    public FollowEntity convertToEntity(){
        return FollowEntity.builder()
                .id(this.id)
                .followee(this.followee.convertToEntity())
                .follower(this.follower.convertToEntity())
                .build();

    }


}