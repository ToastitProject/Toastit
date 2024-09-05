package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.repository.FollowRepository;
import alcoholboot.toastit.feature.user.service.FollowService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 팔로우 관련 비즈니스 로직을 처리하는 서비스 구현 클래스.
 * 사용자가 다른 사용자를 팔로우하거나 언팔로우하는 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    // 팔로우 관련 데이터베이스 작업을 처리하는 리포지토리
    private final FollowRepository userFollowRepository;

    /**
     * 새로운 팔로우 관계를 저장하는 메서드.
     *
     * @param followEntity 팔로우 엔티티 객체
     */
    @Override
    public void follow(FollowEntity followEntity) {
        userFollowRepository.save(followEntity);
    }

    /**
     * 기존 팔로우 관계를 삭제하는 메서드.
     *
     * @param followEntity 삭제할 팔로우 엔티티 객체
     */
    @Override
    public void unfollow(FollowEntity followEntity) {
        userFollowRepository.delete(followEntity);
    }

    /**
     * 팔로워와 팔로우 대상자의 ID로 팔로우 관계를 조회하는 메서드.
     *
     * @param followerId 팔로우하는 사용자의 ID
     * @param followeeId 팔로우 대상자의 ID
     * @return 팔로우 엔티티 객체
     */
    @Override
    public FollowEntity findByFollowerIdAndFolloweeId(Long followerId, Long followeeId) {
        return userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    /**
     * 특정 사용자가 팔로우한 모든 대상자의 ID 목록을 조회하는 메서드.
     *
     * @param followerId 팔로우하는 사용자의 ID
     * @return 팔로우 대상자들의 ID 목록
     */
    @Override
    public List<Long> findFolloweeIdsByFollowerId(Long followerId) {
        return userFollowRepository.findFolloweeIdsByFollowerId(followerId);
    }
}