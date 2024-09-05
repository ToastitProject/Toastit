package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.user.entity.FollowEntity;

import lombok.*;

/**
 * 팔로우 정보를 담는 도메인 클래스.
 * 팔로우 정보를 엔티티로 변환할 수 있습니다.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Follow {

    private Long id;          // 팔로우 ID
    private Long followerId;   // 팔로우를 하는 사용자 ID
    private Long followeeId;   // 팔로우를 받는 사용자 ID

    /**
     * Follow 도메인 객체를 FollowEntity 엔티티로 변환하는 메서드.
     *
     * @return FollowEntity 객체
     */
    public FollowEntity convertToEntity() {
        return FollowEntity.builder()
                .id(this.id)
                .follower(this.convertToEntity().getFollower())   // 팔로워 정보를 엔티티로 변환
                .followee(this.convertToEntity().getFollowee())   // 팔로우 받는 사용자 정보를 엔티티로 변환
                .build();
    }
}