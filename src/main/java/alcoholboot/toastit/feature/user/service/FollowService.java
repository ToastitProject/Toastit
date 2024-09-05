package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.entity.FollowEntity;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 팔로우 기능과 관련된 비즈니스 로직을 제공하는 서비스 인터페이스.
 * 사용자가 다른 사용자를 팔로우하거나 언팔로우하는 기능을 정의합니다.
 */
@Service
public interface FollowService {

    /**
     * 새로운 팔로우 관계를 저장하는 메서드.
     *
     * @param followEntity 팔로우 엔티티 객체
     */
    void follow(FollowEntity followEntity);

    /**
     * 기존 팔로우 관계를 삭제하는 메서드.
     *
     * @param followEntity 삭제할 팔로우 엔티티 객체
     */
    void unfollow(FollowEntity followEntity);

    /**
     * 팔로워와 팔로우 대상자의 ID로 팔로우 관계를 조회하는 메서드.
     *
     * @param followerId 팔로우하는 사용자의 ID
     * @param followeeId 팔로우 대상자의 ID
     * @return 팔로우 엔티티 객체
     */
    FollowEntity findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    /**
     * 특정 사용자가 팔로우한 대상자의 ID 목록을 조회하는 메서드.
     *
     * @param followerId 팔로우하는 사용자의 ID
     * @return 팔로우 대상자들의 ID 목록
     */
    List<Long> findFolloweeIdsByFollowerId(Long followerId);
}