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
    //의존성 주입
    private final FollowRepository userFollowRepository;

    /**
     * 상대방을 팔로우 할 경우, 팔로우 객체를 저장합니다.
     * @param followEntity : 새로운 팔로우 객체입니다
     */
    @Override
    public void follow(FollowEntity followEntity) {
        userFollowRepository.save(followEntity);
    }

    /**
     * 상대방을 팔로우 취소할 경우, 팔로우 객체를 삭제합니다.
     * @param followEntity : 삭제할 팔로우 객체입니다.
     */
    @Override
    public void unfollow(FollowEntity followEntity) {
        userFollowRepository.delete(followEntity);
    }

    /**
     * follower 의 ID 와 followee 의 ID 모두 갖고 있는 팔로우 객체를 찾는 메서드 입니다.
     * @param followerId : 팔로우 하는 사람(본인)의 아이디 입니다.
     * @param followeeId : 팔로우 하고 있는 사람(타인)의 아이디 입니다.
     * @return : 팔로우 객체를 반환합니다.
     */
    @Override
    public FollowEntity findByFollowerIdAndFolloweeId(Long followerId, Long followeeId) {
        return userFollowRepository.findByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    /**
     * 팔로우 하는 사람(본인)의 아이디로 팔로우 하는 타인의 아이디들을 찾는 메서드 입니다.
     * @param followerId : 로그인 한 본인의 아이디 입니다
     * @return : 본인이 팔로우 하고 있는 사람들의 아이디 List 를 반환합니다.
     */
    @Override
    public List<Long> findFolloweeIdsByFollowerId(Long followerId) {
        return userFollowRepository.findFolloweeIdsByFollowerId(followerId);
    }
}