package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.customcocktail.service.CustomCocktailService;
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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final UserService userService;
    private final CustomCocktailService customCocktailService;


    @PostMapping("/follow")
    public String Follow (@RequestParam("nickname") String nickname) { //현재 접속한 다른 User 의 프로필 페이지에서 id를 가져와야 한다.
        log.info("Follow 포스트 매핑 요청이 들어옴");
        //접속한 User 의 정보를 이메일로 찾는다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userService.findByEmail(loginUserEmail);

        //팔로우 할 User 의 정보를 닉네임으로 찾는다.
        Optional<User> followUser = userService.findByNickname(nickname);

        log.info("접속한 사람의 닉네임 : " + loginUser.get().getNickname());
        log.info("팔로우 할 사람의 닉네임 : " + nickname);

        //접속한 user id 와 팔로우할 사람의 id 로 조회하여 FollowEntity 를 생성한다.
        FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(loginUser.get().getId(),followUser.get().getId());

        if (alreadyFollow != null) { //이미 팔로우 중이라면, 팔로우 객체를 삭제한다
            log.info("이미 팔로우 중이라 팔로우를 삭제");
            followService.unfollow(alreadyFollow);
        }
        //아나라면 새로운 팔로우 객체를 하나 생성한다.
        else {
            log.info("조회된 정보가 없으므로, 팔로우를 수행한다.");
            FollowEntity follow = new FollowEntity();
            follow.setFollower(loginUser.get().convertToEntity());
            follow.setFollowee(followUser.get().convertToEntity());
            followService.follow(follow);
        }
        log.info("팔로우 정보 변경 완료");
        return "redirect:/follow";
        }

        @GetMapping("/follow")
        public String following (Model model) {
        log.info("팔로우 페이지로 getMapping 요청이 들어옴.");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loginUserEmail = authentication.getName();
            Optional<User> loginUser = userService.findByEmail(loginUserEmail);

            List<Long> followeeIds = followService.findFolloweeIdsByFollowerId(loginUser.get().getId());
            log.info("로그인 한 사람의 정보로 찾은 팔로우 한 사람들 목록 : "+followeeIds.toString());

            if (!followeeIds.isEmpty()) {
                List<CustomCocktail> cocktails = customCocktailService.getCocktailsByUserIds(followeeIds);
                log.info("팔로우 한 사람의 칵테일 정보들 : " + cocktails.toString());
                model.addAttribute("cocktails", cocktails);
            } else {
                log.warn("팔로우한 사용자가 없습니다.");
                model.addAttribute("cocktails", new ArrayList<>());
            }

        return "feature/user/followingForm";
        }
    }

