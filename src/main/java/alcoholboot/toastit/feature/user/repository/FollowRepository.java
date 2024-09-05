package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.user.entity.FollowEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 팔로우와 관련된 데이터베이스 작업을 처리하는 리포지토리 인터페이스.
 * 팔로우 정보 조회 및 팔로우 관계에 대한 쿼리 메서드를 제공합니다.
 */
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    /**
     * 특정 팔로워와 팔로우 대상자의 팔로우 관계를 조회하는 메서드.
     *
     * @param followerId 팔로워의 ID
     * @param followeeId 팔로우 대상자의 ID
     * @return 팔로우 관계 엔티티
     */
    FollowEntity findByFollowerIdAndFolloweeId(long followerId, long followeeId);

    /**
     * 특정 사용자가 팔로우한 대상자들의 ID 목록을 조회하는 메서드.
     *
     * @param followerId 팔로워의 ID
     * @return 팔로우 대상자들의 ID 목록
     */
    @Query("SELECT f.followee.id FROM FollowEntity f WHERE f.follower.id = :followerId")
    List<Long> findFolloweeIdsByFollowerId(@Param("followerId") Long followerId);
}