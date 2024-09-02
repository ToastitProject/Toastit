package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.user.entity.FollowEntity;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    private Long id;
    private Long followerId;
    private Long followeeId;

    public FollowEntity convertToEntity() {
        return  FollowEntity.builder()
                .id(this.id)
                .follower(this.convertToEntity().getFollower())
                .followee(this.convertToEntity().getFollowee())
                .build();
    }
}