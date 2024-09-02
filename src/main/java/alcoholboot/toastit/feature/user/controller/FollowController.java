package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.service.CraftCocktailService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.service.FollowService;
import alcoholboot.toastit.feature.user.service.UserManagementService;

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

@Slf4j
@Controller
@RequiredArgsConstructor
public class FollowController {

    // 서비스 의존성 주입
    private final FollowService followService;
    private final UserManagementService userManagementService;
    private final CraftCocktailService craftCocktailService;

    // 팔로우 요청을 처리하는 메서드
    @PostMapping("/follow")
    public ResponseEntity<?> Follow(@RequestParam("nickname") String nickname) {
        log.debug("Follow 포스트 매핑 요청이 들어옴");

        // 현재 로그인한 사용자의 이메일을 통해 User 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

        // 팔로우할 사용자의 닉네임으로 User 정보를 가져옴
        Optional<User> followUser = userManagementService.findByNickname(nickname);

        log.debug("접속한 사람의 닉네임 : " + loginUser.get().getNickname());
        log.debug("팔로우 할 사람의 닉네임 : " + nickname);

        // 팔로우 관계가 이미 존재하는지 확인
        FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(loginUser.get().getId(), followUser.get().getId());

        // 이미 팔로우 중이라면 언팔로우 처리
        if (alreadyFollow != null) {
            log.debug("이미 팔로우 중이라 팔로우를 삭제");
            followService.unfollow(alreadyFollow);
            return ResponseEntity.ok("unfollow");
        }
        // 팔로우가 아닌 경우, 새로운 팔로우 객체를 생성하여 저장
        else {
            log.debug("조회된 정보가 없으므로, 팔로우를 수행한다.");
            FollowEntity follow = new FollowEntity();
            follow.setFollower(loginUser.get().convertToEntity());
            follow.setFollowee(followUser.get().convertToEntity());
            followService.follow(follow);
            return ResponseEntity.ok("follow");
        }
    }

    // 팔로우한 사용자들의 레시피를 보여주는 페이지로 이동하는 메서드
    @GetMapping("/follow")
    public String following(Model model) {
        log.debug("팔로우 페이지로 getMapping 요청이 들어옴.");

        // 현재 로그인한 사용자의 이메일을 통해 User 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

        // 로그인한 사용자가 팔로우한 사용자들의 ID를 가져옴
        List<Long> followeeIds = followService.findFolloweeIdsByFollowerId(loginUser.get().getId());
        log.debug("로그인 한 사람의 정보로 찾은 팔로우 한 사람들 목록 : " + followeeIds.toString());

        // 팔로우한 사용자들의 칵테일 정보를 가져와 모델에 추가
        if (!followeeIds.isEmpty()) {
            List<CraftCocktailEntity> cocktails = craftCocktailService.getCocktailsByUserIds(followeeIds);
            log.debug("팔로우 한 사람의 칵테일 정보들 : " + cocktails.toString());
            model.addAttribute("cocktails", cocktails);
        } else {
            log.warn("팔로우한 사용자가 없습니다.");
            model.addAttribute("cocktails", new ArrayList<>());
        }

        return "user/following-recipes";
    }
}