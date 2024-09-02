package alcoholboot.toastit.feature.basecocktail.controller;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.service.LikeService;
import alcoholboot.toastit.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cocktails")
public class CocktailController {
    private final CocktailService cocktailService;
    private final LikeService likeService;
    private final UserService userService;

    /**
     * 모든 칵테일 목록을 페이지네이션하여 조회합니다.
     *
     * @param page 조회할 페이지 번호
     * @param model Spring MVC 모델
     * @return 칵테일 목록 뷰 이름
     */
    @GetMapping("/all")
    public String getAllCocktails(
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        Page<Cocktail> cocktails = cocktailService.getAllCocktailsPaged(PageRequest.of(page, 20)); // 20개씩 페이징
        model.addAttribute("cocktails", cocktails);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cocktails.getTotalPages());
        return "feature/basecocktail/allList-form";
    }


    /**
     * 복합 조건(재료, 잔, 타입)으로 칵테일을 필터링하여 조회합니다.
     *
     * @param page 조회할 페이지 번호
     * @param ingredient 필터링할 재료 목록
     * @param glass 필터링할 잔 종류
     * @param type 필터링할 칵테일 타입
     * @param model Spring MVC 모델
     * @return 필터링된 칵테일 목록 뷰 이름
     */
    @GetMapping("/complex")
    public String getCocktailsByComplex(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) List<String> ingredient,
            @RequestParam(required = false) String glass,
            @RequestParam(required = false) String type,
            Model model) {

        Page<Cocktail> cocktails = cocktailService.getCocktailsByFilterPaged(
                ingredient, glass, type, PageRequest.of(page, 20));

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cocktails.getTotalPages());
        model.addAttribute("cocktails", cocktails.getContent());

        // 입력된 값만 모델에 추가
        if (ingredient != null && !ingredient.isEmpty()) {
            model.addAttribute("ingredient", String.join(", ", ingredient));
        }
        if (glass != null && !glass.isEmpty()) {
            model.addAttribute("glass", String.join(", ", glass));
        }
        if (type != null && !type.isEmpty()) {
            model.addAttribute("type", String.join(", ", type));
        }

        return "feature/basecocktail/search-form";
    }

    /**
     * 특정 ID의 칵테일 상세 정보를 조회합니다.
     *
     * @param id 조회할 칵테일의 ID
     * @param model Spring MVC 모델
     * @param redirectAttributes 리다이렉트 시 사용할 속성
     * @return 칵테일 상세 정보 뷰 이름
     */
    @GetMapping("/id")
    public String getCocktailById(
            @RequestParam("id") String id,
            Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Cocktail> cocktail = cocktailService.getCocktailById(new ObjectId(id));
        model.addAttribute("cocktail", cocktail);

        ObjectId basecocktailId = new ObjectId(id);
        int likeCount = likeService.countByBasecocktailsId(basecocktailId);
        model.addAttribute("likeCount", likeCount);

        // 로그인한 사용자의 좋아요 상태 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String loginUserEmail = authentication.getName();
            Optional<User> loginUser = userService.findByEmail(loginUserEmail);

            if (loginUser.isPresent()) {
                LikeEntity existingLike = likeService.findByUserIdAndBasecocktailsId(loginUser.get().getId(), basecocktailId);
                model.addAttribute("isLiked", existingLike != null); // 좋아요 여부 추가
            } else {
                model.addAttribute("isLiked", false); // 로그인 사용자 없음
            }
        } else {
            model.addAttribute("isLiked", false); // 로그인하지 않은 경우
        }

        return "feature/basecocktail/detail-view";
    }

    /**
     * 지정된 개수만큼 랜덤 칵테일을 조회합니다.
     *
     * @param count 조회할 랜덤 칵테일의 개수
     * @param model Spring MVC 모델
     * @return 랜덤 칵테일 목록 뷰 이름
     */
    @GetMapping("/random")
    public String getRandom(@RequestParam(defaultValue = "5") int count,
                            Model model) {
        List<Cocktail> cocktails= cocktailService.getRandomCocktails(count);
        model.addAttribute("cocktails", cocktails);

        return "feature/basecocktail/random-view";
    }

    // do not Delete
//    @GetMapping("/name")
//    public String getName(@RequestParam("name") String name,
//                          Model model) {
//        Cocktail cocktail = cocktailService.getSingleCocktailByName(name);
//        model.addAttribute("cocktail", cocktail);
//
//        return "feature/basecocktail/name";
//    }
}
