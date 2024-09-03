package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.repository.FollowRepository;
import alcoholboot.toastit.feature.user.service.FollowService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository userFollowRepository;

    @Override
    public void follow(FollowEntity followEntity) {
        userFollowRepository.save(followEntity);
    }

    @Override
    public void unfollow(FollowEntity followEntity) {
        userFollowRepository.delete(followEntity);
    }

    @Override
    public FollowEntity findByFollowerIdAndFolloweeId(Long followerId, Long followeeId) {
        return userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Override
    public List<Long> findFolloweeIdsByFollowerId(Long followerId) {
        return userFollowRepository.findFolloweeIdsByFollowerId(followerId);
    }
}