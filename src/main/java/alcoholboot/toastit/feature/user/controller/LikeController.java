
package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.service.impl.CraftCocktailServiceImpl;
import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.LikeEntity;

import alcoholboot.toastit.feature.user.service.LikeService;
import alcoholboot.toastit.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

import alcoholboot.toastit.global.entity.JpaAuditingFields;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LikeController {
    private final UserService userService;
    private final LikeService likeService;
    private final CraftCocktailServiceImpl craftCocktailService;

    /**
     * 사용자가 입력한 커스텀 칵테일 레시피에 좋아요를 할 수 있는 기능입니다.
     * @param : 클라이언트가 요청 본문에 커스텀 칵테일 레시피의 이름을 서버로 전송합니다.
     * @return : 클라이언트에게 좋아요 요청에 대한 응답을 보내줍니다. (이미 좋아요를 한 경우 취소됨)
     */
    @PostMapping("/like")
    public ResponseEntity<?> likeCocktail(@RequestBody Map<String, String> requestBody) {
        log.debug("사용자가 커스텀 레시피에 좋아요 버튼을 눌렀습니다");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userService.findByEmail(loginUserEmail);

        String cocktailName = requestBody.get("drink-name");

        CraftCocktailEntity cocktail = craftCocktailService.findIdByName(cocktailName);

        //로그인 한 유저가 해당 레피피를 좋아요를 눌렀는지 확인한다 (좋아요 객체가 있나 확인)
        LikeEntity existingLike = likeService.findByUserIdAndCraftCocktailId(loginUser.get().getId(), cocktail.getId());

        if (existingLike != null) {
            // 기존 좋아요가 존재하면 삭제한다
            likeService.deleteLike(existingLike);
            return ResponseEntity.ok("좋아요가 취소되었습니다");
        } else {
            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setCraftCocktail(cocktail);
            likeService.saveLike(like);
            return ResponseEntity.ok("좋아요가 추가되었습니다");
        }
    }

    /**
     * 기본 칵테일 레시피에 좋아요 를 할 수 있는 기능입니다
     * @param requestBody : 클라이언트가 요청 본문에 기본 칵테일 레시피의 아이디를 담아 보냅니다
     * @return : 클라이언트에게 좋아요 요청에 대한 응답을 보내줍니다. (이미 좋아요를 한 경우 취소됨)
     */
    @PostMapping("/baseLike")
    public ResponseEntity<?> likebasecocktail(@RequestBody Map<String, String> requestBody) {
        log.debug("사용자가 기본 레시피에 좋아요 버튼을 눌렀습니다");

        // 로그인 한 user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userService.findByEmail(loginUserEmail);

        // 좋아요 할 칵테일
        String basecocktailIdstr = requestBody.get("base-cocktail-number");
        ObjectId basecocktailId = new ObjectId(basecocktailIdstr);

        // 이미 좋아요가 있는지 확인하는 객체 생성
        LikeEntity existingLike = likeService.findByUserIdAndBasecocktailsId(loginUser.get().getId(),basecocktailId);

        if (existingLike != null) {
            // 기존 좋아요가 존재하면 삭제
            likeService.deleteLike(existingLike);
            return ResponseEntity.ok("좋아요가 취소되었습니다");
        } else {
            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setBasecocktailsId(basecocktailId);
            likeService.saveLike(like);

            return ResponseEntity.ok("좋아요가 추가되었습니다.");
        }
    }
}


