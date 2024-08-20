package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.entity.FollowEntity;
import org.springframework.stereotype.Service;

@Service
public interface FollowService {
    void follow(FollowEntity followEntity);
    void unfollow(FollowEntity followEntity);
    FollowEntity findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
