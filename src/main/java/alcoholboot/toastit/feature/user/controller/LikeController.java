package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.service.CraftCocktailService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.service.LikeService;
import alcoholboot.toastit.feature.user.service.UserManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * 좋아요 기능을 처리하는 컨트롤러.
 * 사용자는 커스텀 칵테일과 기본 칵테일 레시피에 좋아요를 추가하거나 취소할 수 있습니다.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class LikeController {

    private final UserManagementService userManagementService;
    private final LikeService likeService;
    private final CraftCocktailService craftCocktailServiceCocktail;

    /**
     * 사용자가 커스텀 칵테일 레시피에 좋아요를 추가하거나, 이미 좋아요한 경우 좋아요를 취소하는 메서드.
     *
     * @param requestBody 요청 본문에서 커스텀 칵테일 이름을 전달받습니다.
     * @return 좋아요 추가 또는 취소 결과를 반환합니다.
     */
    @PostMapping("/like")
    public ResponseEntity<?> likeCocktail(@RequestBody Map<String, String> requestBody) {
        log.debug("사용자가 커스텀 칵테일 레시피에 좋아요 버튼을 눌렀습니다.");

        // 현재 로그인한 사용자 정보 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

        String cocktailName = requestBody.get("drink-name");

        // 칵테일 이름으로 칵테일 엔티티를 조회
        CraftCocktailEntity cocktail = craftCocktailServiceCocktail.findIdByName(cocktailName);

        // 좋아요 여부 확인
        LikeEntity existingLike = likeService.findByUserIdAndCraftCocktailId(loginUser.get().getId(), cocktail.getId());

        if (existingLike != null) {
            // 기존 좋아요가 존재하면 삭제
            likeService.deleteLike(existingLike);
            return ResponseEntity.ok("좋아요가 취소되었습니다");
        } else {
            // 좋아요가 없으면 새로 추가
            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setCraftCocktail(cocktail);
            likeService.saveLike(like);
            return ResponseEntity.ok("좋아요가 추가되었습니다");
        }
    }

    /**
     * 사용자가 기본 칵테일 레시피에 좋아요를 추가하거나, 이미 좋아요한 경우 좋아요를 취소하는 메서드.
     *
     * @param requestBody 요청 본문에서 기본 칵테일의 ID를 전달받습니다.
     * @return 좋아요 추가 또는 취소 결과를 반환합니다.
     */
    @PostMapping("/baseLike")
    public ResponseEntity<?> likeDefaultCocktail(@RequestBody Map<String, String> requestBody) {
        log.debug("사용자가 기본 칵테일에 좋아요 버튼을 눌렀습니다.");

        // 현재 로그인한 사용자 정보 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

        // 기본 칵테일 ID
        String basecocktailIdstr = requestBody.get("base-cocktail-number");
        ObjectId basecocktailId = new ObjectId(basecocktailIdstr);

        // 좋아요 여부 확인
        LikeEntity existingLike = likeService.findByUserIdAndBasecocktailsId(loginUser.get().getId(), basecocktailId);

        if (existingLike != null) {
            // 기존 좋아요가 존재하면 삭제
            likeService.deleteLike(existingLike);
            return ResponseEntity.ok("좋아요가 취소되었습니다");
        } else {
            // 좋아요가 없으면 새로 추가
            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setBasecocktailsId(basecocktailId);
            likeService.saveLike(like);

            return ResponseEntity.ok("좋아요가 추가되었습니다.");
        }
    }
}