package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.user.entity.FollowEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    FollowEntity findByFollowerIdAndFolloweeId(long followerId, long followeeId);

    @Query("SELECT f.followee.id FROM FollowEntity f WHERE f.follower.id = :followerId")
    List<Long> findFolloweeIdsByFollowerId(@Param("followerId") Long followerId);
}