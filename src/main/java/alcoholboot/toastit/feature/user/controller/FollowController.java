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

/**
 * 팔로우 기능과 관련된 요청을 처리하는 컨트롤러.
 * 사용자는 다른 사용자를 팔로우하거나 팔로잉한 사용자들의 레시피를 볼 수 있습니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserManagementService userManagementService;
    private final CraftCocktailService craftCocktailService;

    /**
     * 사용자가 다른 사용자를 팔로우하거나, 이미 팔로우한 경우 팔로우를 취소하는 메서드.
     *
     * @param nickname 팔로우하려는 사용자의 닉네임
     * @return 팔로우 또는 언팔로우 결과를 반환
     */
    @PostMapping("/follow")
    public ResponseEntity<?> Follow(@RequestParam("nickname") String nickname) {
        log.debug("사용자가 FOLLOW 버튼을 눌렀습니다.");

        // 현재 로그인한 사용자의 이메일을 통해 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

        // 팔로우할 사용자의 닉네임으로 사용자 정보를 가져옴
        Optional<User> followUser = userManagementService.findByNickname(nickname);

        // 팔로우 상태를 확인 (이미 팔로우 중인 경우)
        FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(loginUser.get().getId(), followUser.get().getId());

        if (alreadyFollow != null) {
            // 이미 팔로우 중인 경우 언팔로우 처리
            followService.unfollow(alreadyFollow);
            return ResponseEntity.ok("unfollow");
        } else {
            // 새로운 팔로우 객체를 생성하여 팔로우 처리
            FollowEntity follow = new FollowEntity();
            follow.setFollower(loginUser.get().convertToEntity());
            follow.setFollowee(followUser.get().convertToEntity());
            followService.follow(follow);
            return ResponseEntity.ok("follow");
        }
    }

    /**
     * 로그인한 사용자가 팔로우한 사용자들의 커스텀 칵테일 레시피를 조회하는 메서드.
     *
     * @param model 팔로우한 사용자들의 커스텀 칵테일 레시피 목록을 뷰에 전달하기 위한 모델 객체
     * @return 팔로우한 사용자들의 커스텀 레시피 페이지로 이동
     */
    @GetMapping("/follow")
    public String following(Model model) {
        log.debug("사용자가 팔로잉 페이지로 접근합니다.");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

        // 로그인한 사용자가 팔로우한 사용자들의 ID를 조회
        List<Long> followeeIds = followService.findFolloweeIdsByFollowerId(loginUser.get().getId());

        if (!followeeIds.isEmpty()) {
            // 팔로우한 사용자들의 커스텀 칵테일 레시피를 조회하여 모델에 추가
            List<CraftCocktailEntity> cocktails = craftCocktailService.getCocktailsByUserIds(followeeIds);
            model.addAttribute("cocktails", cocktails);
        } else {
            model.addAttribute("cocktails", new ArrayList<>());
        }
        return "user/following-recipes";
    }
}