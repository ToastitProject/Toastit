package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.repository.FollowRepository;
import alcoholboot.toastit.feature.user.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;

    @Override
    public void follow(FollowEntity followEntity) {
        followRepository.save(followEntity);
    }

    @Override
    public void unfollow(FollowEntity followEntity) {
        followRepository.delete(followEntity);
    }

    @Override
    public FollowEntity findByFollowerIdAndFolloweeId(Long followerId, Long followeeId) {
        return followRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
    }
}
