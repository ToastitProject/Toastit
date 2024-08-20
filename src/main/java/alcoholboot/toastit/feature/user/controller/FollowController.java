package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.user.domain.Follow;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.repository.FollowRepository;
import alcoholboot.toastit.feature.user.service.FollowService;
import alcoholboot.toastit.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final UserService userService;
    private final FollowRepository followRepository;

    @PostMapping("/follow")
    public void Follow (@RequestParam("userId") UserEntity followerEntity) { //현재 접속한 다른 User 의 프로필 페이지에서 id를 가져와야 한다.
        //접속한 User 의 정보를 이메일로 찾는다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userService.findByEmail(loginUserEmail);

        //접속한 User 가 해당 user 를 팔로우 하고 있는지 boolean 값으로 가져온다.
        boolean isFollowing = followRepository.findByFollowerAndFollowee(loginUser.get().convertToEntity(), followerEntity);

        //접속한 user id 와 팔로우할 사람의 id 로 조회하여 FollowEntity 를 생성한다.
        FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(loginUser.get().getId(),followerEntity.getId());

        if (isFollowing) { //이미 팔로우 중이라면, 팔로우 객체를 삭제한다
            followService.unfollow(alreadyFollow);
        }
        //아나라면 새로운 팔로우 객체를 하나 생성한다.
        else {
            Follow follow = new Follow();
            follow.setFollowerId(loginUser.get().convertToEntity().getId());
            follow.setFolloweeId(followerEntity.getId());
            followService.follow(follow.convertToEntity());
        }
        }
    }

