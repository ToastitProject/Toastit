
package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.craftcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.craftcocktail.service.CustomCocktailService;
import alcoholboot.toastit.feature.defaultcocktail.domain.Cocktail;
import alcoholboot.toastit.feature.defaultcocktail.service.CocktailService;
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

@Controller
@Slf4j
@RequiredArgsConstructor
public class LikeController {
    private final UserService userService;
    private final LikeService likeService;
    private final CustomCocktailService customCocktailService;
    private final CocktailService cocktailService;

    //커스텀 칵테일에 좋아요를 표시하는 기능
    @PostMapping("/like")
    public ResponseEntity<?> likeCocktail(@RequestBody Map<String, String> requestBody) {
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
            return ResponseEntity.ok("좋아요가 취소되었습니다");
        } else {
            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setCustomCocktail(cocktail);
            likeService.saveLike(like);
            log.info("좋아요가 추가되었습니다. cocktail id: " + cocktail.getId());
            log.info("like 객체에 저장된 login user id : " + like.getUser().getId());
            log.info("like 객체에 저장된 custom cocktail id : " + like.getCustomCocktail().getId());
            return ResponseEntity.ok("좋아요가 추가되었습니다");
        }

    }

    @PostMapping("/defaultLike")
    public ResponseEntity<?> likeDefaultCocktail(@RequestBody Map<String, String> requestBody) {
        log.info("Default cocktail 좋아요 postMapping 전송됨");

        // 로그인 한 user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userService.findByEmail(loginUserEmail);

        // 좋아요 할 칵테일
        String defaultCocktailIdstr = requestBody.get("default-cocktail-number");
        log.info("좋아요 할 기본 칵테일 ID : "+defaultCocktailIdstr);
        ObjectId defaultCocktailId = new ObjectId(defaultCocktailIdstr);

        Optional<Cocktail> cocktail = cocktailService.getCocktailById(defaultCocktailId);

        log.info("로그인 한 User 닉네임 : " + loginUser.get().getNickname());
        log.info("좋아요 할 Default cocktail ID : " + cocktail.get().getId());

        // 이미 좋아요가 있는지 확인하는 객체 생성
        LikeEntity existingLike = likeService.findByUserIdAndDefaultCocktailsId(loginUser.get().getId(),defaultCocktailId);

        if (existingLike != null) {
            // 기존 좋아요가 존재하면 삭제
            log.info("Default cocktail 에 좋아요가 확인 됨");
            likeService.deleteLike(existingLike);
            log.info("기존 좋아요가 취소되었습니다. cocktail id: " + cocktail.get().getId());
            return ResponseEntity.ok("좋아요가 취소되었습니다");
        } else {
            log.info("기존에 존재하는 좋아요 없음이 확인 됨");
            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setDefaultCocktailsId(defaultCocktailId);
            likeService.saveLike(like);
            log.info("좋아요가 추가되었습니다. cocktail id: " + cocktail.get().getId());
            log.info("like 객체에 저장된 login user id : " + like.getUser().getId());
            return ResponseEntity.ok("좋아요가 추가되었습니다.");
        }
    }

}


