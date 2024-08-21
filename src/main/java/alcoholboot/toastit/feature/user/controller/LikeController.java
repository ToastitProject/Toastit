
package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.customcocktail.service.CustomCocktailService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.LikeEntity;

import alcoholboot.toastit.feature.user.service.LikeService;
import alcoholboot.toastit.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LikeController {
    private final UserService userService;
    private final LikeService likeService;
    private final CustomCocktailService customCocktailService;

    @PostMapping("/like")
    public String likeCocktail(@RequestBody Map<String, String> requestBody) {
        log.info("좋아요 post 요청이 컨트롤러로 연결되었습니다");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userService.findByEmail(loginUserEmail);

        String cocktailName = requestBody.get("drink-name");
        CustomCocktail cocktail = customCocktailService.findIdByName(cocktailName);

        log.info("현재 로그인 한 user email : " + loginUserEmail);
        log.info("좋아요 할 칵테일 레시피의 ID :" + cocktail.getId());
        log.info("좋아요 할 칵테일 레시피의 name :" + cocktail.getName());

        LikeEntity existingLike = likeService.findByUserIdAndCustomCocktailId(loginUser.get().getId(), cocktail.getId());

        if (existingLike != null) {
            // 기존 좋아요가 존재하면 삭제
            likeService.deleteLike(existingLike);
            log.info("기존 좋아요가 취소되었습니다. cocktail id: " + cocktail.getId());
        } else {
            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setCustomCocktail(cocktail);
            likeService.saveLike(like);
            log.info("좋아요가 추가되었습니다. cocktail id: " + cocktail.getId());
            log.info("like 객체에 저장된 login user id : " + like.getUser().getId());
            log.info("like 객체에 저장된 custom cocktail id : " + like.getCustomCocktail().getId());
        }
        return "redirect:/user/mypage";
    }
}


