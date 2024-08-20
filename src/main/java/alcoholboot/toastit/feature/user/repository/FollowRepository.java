package alcoholboot.toastit.feature.user.repository;

import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    boolean findByFollowerAndFollowee(UserEntity loginUserEntity, UserEntity followeeUserEntity);
    FollowEntity findByFollowerIdAndFolloweeId(long followerId, long followeeId);
}
