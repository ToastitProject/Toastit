package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.service.impl.CraftCocktailServiceImpl;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.service.FollowService;
import alcoholboot.toastit.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    private final CraftCocktailServiceImpl customCocktailService;

    /**
     * 다른 사용자를 팔로우 할 수 있는 기능입니다.
     * @param nickname : 팔로우 하고자 하는 유저의 닉네임 정보 입니다.
     * @return : 팔로우 요청에 대한 응답을 보내줍니다 (이미 팔로우를 하고 있으면 취소)
     */
    @PostMapping("/follow")
    public ResponseEntity<?> Follow (@RequestParam("nickname") String nickname) { //현재 접속한 다른 User 의 프로필 페이지에서 id를 가져와야 한다.
        log.debug("사용자가 FOLLOW 버튼을 눌렀습니다.");
        //접속한 User 의 정보를 이메일로 찾는다
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userService.findByEmail(loginUserEmail);

        //팔로우 할 User 의 정보를 닉네임으로 찾는다.
        Optional<User> followUser = userService.findByNickname(nickname);

        //접속한 user id 와 팔로우할 사람의 id 로 조회하여 FollowEntity 를 생성한다. (이미 팔로우 중인지 체크)
        FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(loginUser.get().getId(),followUser.get().getId());

        if (alreadyFollow != null) {
            //이미 팔로우 중이라면, 팔로우 객체를 삭제한다
            followService.unfollow(alreadyFollow);
            return ResponseEntity.ok("unfollow");
        }
        else {
            //아나라면 새로운 팔로우 객체를 하나 생성한다.
            FollowEntity follow = new FollowEntity();
            follow.setFollower(loginUser.get().convertToEntity());
            follow.setFollowee(followUser.get().convertToEntity());
            followService.follow(follow);
            return ResponseEntity.ok("follow");
        }
        }

    /**
     * 로그인 한 유저가 자신이 팔로우 한 사용자들의 커스텀 칵테일 레시피들을 볼 수 있는 페이지로 이동할 수 있는 기능입니다.
     * @param model : 팔로우 한 유저의 커스텀 칵테일 레시피들을 화면으로 보내주는 객체입니다.
     * @return : 커스텀 레시피륻을 볼 수 있는 페이지로 이동합니다.
     */
    @GetMapping("/follow")
        public String following (Model model) {
        log.debug("팔로우 페이지로 getMapping 요청이 들어옴.");
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loginUserEmail = authentication.getName();
            Optional<User> loginUser = userService.findByEmail(loginUserEmail);
            //로그인 한 user 가 팔로우 한 user id 를 조회
            List<Long> followeeIds = followService.findFolloweeIdsByFollowerId(loginUser.get().getId());

            if (!followeeIds.isEmpty()) {
                //팔로우 하고 있는 사람이 존재한다면 그 레시피드들을 view 로 보내준다.
                List<CraftCocktailEntity> cocktails = customCocktailService.getCocktailsByUserIds(followeeIds);
                model.addAttribute("cocktails", cocktails);
            } else {
                model.addAttribute("cocktails", new ArrayList<>());
            }

        return "feature/user/followingForm";
        }
    }

