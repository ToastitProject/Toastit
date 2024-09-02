package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.service.impl.CraftCocktailServiceImpl;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LikeController {

    // 서비스 의존성 주입
    private final UserManagementService userManagementService;
    private final LikeService likeService;
    private final CraftCocktailServiceImpl craftCocktailServiceCocktail;
    private final CocktailService cocktailService;

    // 커스텀 칵테일에 좋아요를 표시하는 메서드
    @PostMapping("/like")
    public ResponseEntity<?> likeCocktail(@RequestBody Map<String, String> requestBody) {
        log.debug("좋아요 post 요청이 컨트롤러로 연결되었습니다");

        // 현재 로그인한 사용자의 이메일을 통해 User 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

        // 요청 본문에서 칵테일 이름을 가져옴
        String cocktailName = requestBody.get("drink-name");

        // 칵테일 이름으로 칵테일 엔티티를 조회
        CraftCocktailEntity cocktail = craftCocktailServiceCocktail.findIdByName(cocktailName);

        log.debug("현재 로그인 한 user email : " + loginUserEmail);
        log.debug("좋아요 할 칵테일 레시피의 ID :" + cocktail.getId());
        log.debug("좋아요 할 칵테일 레시피의 name :" + cocktail.getName());

        // 로그인한 사용자와 칵테일 ID로 기존 좋아요가 있는지 확인
        LikeEntity existingLike = likeService.getByUserIdAndCraftCocktailId(loginUser.get().getId(), cocktail.getId());

        if (existingLike != null) {
            // 기존 좋아요가 존재하면 삭제
            likeService.deleteLike(existingLike);
            log.debug("기존 좋아요가 취소되었습니다. cocktail id: " + cocktail.getId());

            return ResponseEntity.ok("좋아요가 취소되었습니다");
        } else {
            // 좋아요가 없으면 새로운 좋아요 엔티티 생성 및 저장
            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setCraftCocktail(cocktail);
            likeService.saveLike(like);

            log.debug("좋아요가 추가되었습니다. cocktail id: " + cocktail.getId());
            log.debug("like 객체에 저장된 login user id : " + like.getUser().getId());
            log.debug("like 객체에 저장된 craft cocktail id : " + like.getCraftCocktail().getId());

            return ResponseEntity.ok("좋아요가 추가되었습니다");
        }
    }

    // 기본 칵테일에 좋아요를 표시하는 메서드
    @PostMapping("/defaultLike")
    public ResponseEntity<?> likeDefaultCocktail(@RequestBody Map<String, String> requestBody) {
        log.debug("Default cocktail 좋아요 postMapping 전송됨");

        // 현재 로그인한 사용자의 이메일을 통해 User 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUserEmail = authentication.getName();
        Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

        // 요청 본문에서 기본 칵테일 ID를 가져옴
        String defaultCocktailIdstr = requestBody.get("default-cocktail-number");
        log.debug("좋아요 할 기본 칵테일 ID : " + defaultCocktailIdstr);
        ObjectId defaultCocktailId = new ObjectId(defaultCocktailIdstr);

        // 기본 칵테일 ID로 기본 칵테일 엔티티를 조회
        Optional<Cocktail> cocktail = cocktailService.getCocktailById(defaultCocktailId);

        log.debug("로그인 한 User 닉네임 : " + loginUser.get().getNickname());
        log.debug("좋아요 할 Default cocktail ID : " + cocktail.get().getId());

        // 로그인한 사용자와 기본 칵테일 ID로 기존 좋아요가 있는지 확인
        LikeEntity existingLike = likeService.findByUserIdAndDefaultCocktailsId(loginUser.get().getId(), defaultCocktailId);

        if (existingLike != null) {
            // 기존 좋아요가 존재하면 삭제
            log.debug("Default cocktail 에 좋아요가 확인 됨");

            likeService.deleteLike(existingLike);

            log.debug("기존 좋아요가 취소되었습니다. cocktail id: " + cocktail.get().getId());
            return ResponseEntity.ok("좋아요가 취소되었습니다");
        } else {
            // 좋아요가 없으면 새로운 좋아요 엔티티 생성 및 저장
            log.debug("기존에 존재하는 좋아요 없음이 확인 됨");

            LikeEntity like = new LikeEntity();
            like.setUser(loginUser.get().convertToEntity());
            like.setDefaultCocktailsId(defaultCocktailId);

            likeService.saveLike(like);

            log.debug("좋아요가 추가되었습니다. cocktail id: " + cocktail.get().getId());
            log.debug("like 객체에 저장된 login user id : " + like.getUser().getId());
            return ResponseEntity.ok("좋아요가 추가되었습니다.");
        }
    }
}