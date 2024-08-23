package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.entity.FollowEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FollowService {
    void follow(FollowEntity followEntity);
    void unfollow(FollowEntity followEntity);
    FollowEntity findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
    List<Long> findFolloweeIdsByFollowerId (Long followerId);
}
