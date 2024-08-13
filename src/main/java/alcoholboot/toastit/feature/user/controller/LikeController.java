package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LikeController {
    private final UserService userService;
    private final LikeService likeService;
    private final CocktailService cocktailService;

    @PostMapping("/like")
    public String like(@RequestParam("cocktailId") String cocktailId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userService.findByEmail(loginUserEmail); //로그인 한 user 를 토큰에 저장된 이메일로 찾아서 생성하기

        Optional<CocktailEntity> likeCocktail = cocktailService.getCocktailById(cocktailId); // 좋아요 할 칵테일의 아이디 가져오기
        CocktailEntity cocktail = likeCocktail.get().convertToDomain().convertToEntity() ; // 좋아요 할 칵테일을 엔티티로 변환

        log.info("현재 로그인 한 user email : " + loginUserEmail);
        log.info("좋아요 할 칵테일 레시피의 ID :" +likeCocktail.get().getId());

        Optional<LikeEntity> existingLike = likeService.findByUserIdAndCocktailId(loginUser.get().getId(), cocktail.getId()); //존재하는 좋아요인가 찾는다, ObjectId 타입 -> long 타입으로 변환

        if (existingLike.isPresent()) {
            // 이미 좋아요가 존재하는 경우, 좋아요 취소
            likeService.delete(existingLike.get().convertToDomain());
            log.info("좋아요가 취소되었습니다. cocktail id: " + cocktail.getId());
        } else {
            // 좋아요가 존재하지 않는 경우, 새로운 좋아요 객체 생성
            LikeEntity like = new LikeEntity(); // 좋아요 객체 생성
            like.setUser(loginUser.get().convertToEntity());
            like.setCocktail(cocktail);
            likeService.save(like.convertToDomain()); // 좋아요 객체 저장
            log.info("좋아요가 추가되었습니다. cocktail id: " + cocktail.getId());
            log.info("like 객체에 저장된 login user id : "+ like.getUser().getId());
            log.info("like 객체에 저장된 cocktail id : "+ like.getCocktail().getId());
        }
        return "redirect:/feature/user/mypage";
    }

}
