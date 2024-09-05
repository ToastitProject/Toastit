package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.basecocktail.entity.CocktailDocument;
import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
import alcoholboot.toastit.feature.craftcocktail.domain.CraftCocktail;
import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.service.CraftCocktailService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.FollowEntity;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.service.FollowService;
import alcoholboot.toastit.feature.user.service.LikeService;
import alcoholboot.toastit.feature.user.service.UserProfileImageService;
import alcoholboot.toastit.feature.user.service.UserManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bson.types.ObjectId;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 사용자와 관련된 요청을 처리하는 컨트롤러.
 * 마이페이지, 프로필 조회, 정보 수정, 좋아요 및 팔로우 관련 기능을 제공합니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserManagementService userManagementService;
    private final CocktailService cocktailService;
    private final CraftCocktailService craftcocktailService;
    private final FollowService followService;
    private final LikeService likeService;
    private final UserProfileImageService userProfileImageService;

    /**
     * 로그인한 사용자의 마이페이지를 보여주는 메서드.
     *
     * @param model 로그인한 사용자의 정보를 담고 있는 모델 객체
     * @return 마이페이지로 이동
     */
    @GetMapping("/mypages")
    public String showMyPages(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userManagementService.findByEmail(email);
            if (userOptional.isPresent()) {
                model.addAttribute("user", userOptional.get());
                model.addAttribute("notLoginUser", false);
            } else {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            }
        } else {
            model.addAttribute("error", "사용자가 인증되지 않았습니다.");
        }

        return "user/mypage-form";
    }

    /**
     * 닉네임을 클릭하여 자신의 정보 또는 다른 사용자의 정보를 보여주는 메서드.
     *
     * @param nickname 정보를 조회할 사용자의 닉네임
     * @param model 조회된 사용자 정보를 담는 모델 객체
     * @return 사용자 정보 페이지로 이동
     */
    @GetMapping("/mypage")
    public String showMyPage(@RequestParam("nickname") String nickname, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> loggedInUserOptional = userManagementService.findByEmail(email);
            if (loggedInUserOptional.isPresent()) {
                User loggedInUser = loggedInUserOptional.get();
                model.addAttribute("loggedInUser", loggedInUser);

                if (loggedInUser.getNickname().equals(nickname)) {
                    model.addAttribute("user", loggedInUser);
                    model.addAttribute("notLoginUser", false);
                } else {
                    Optional<User> otherUserOptional = userManagementService.findByNickname(nickname);
                    if (otherUserOptional.isPresent()) {
                        model.addAttribute("user", otherUserOptional.get());
                        model.addAttribute("notLoginUser", true);

                        FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(
                                loggedInUser.getId(),
                                otherUserOptional.get().getId()
                        );

                        if (alreadyFollow != null) {
                            model.addAttribute("alreadyFollow", alreadyFollow);
                        }
                    } else {
                        model.addAttribute("error", "다른 사용자를 찾을 수 없습니다.");
                    }
                }
            } else {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
            }
        } else {
            return "redirect:/auth/login";
        }

        return "user/mypage-form";
    }


    /**
     * 로그인한 사용자가 자신의 정보를 수정할 수 있는 페이지로 이동하는 메서드.
     *
     * @param model 현재 로그인한 사용자의 정보를 담는 모델 객체
     * @return 정보 수정 페이지로 이동
     */
    @GetMapping("/edit")
    public String showEditPage(Model model) {
        //인증된 사용자인지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {

            String email = authentication.getName();
            Optional<User> user = userManagementService.findByEmail(email);
            //email 로 사용자의 정보를 찾아와 view 로 전달
            if (user.isPresent()) {
                model.addAttribute("user", user.get());
            }
        }
        return "user/edit-form";
    }

    /**
     * 사용자가 자신의 닉네임을 수정하는 메서드.
     *
     * @param nickname 새로운 닉네임
     * @return 수정 완료 후 마이페이지로 리다이렉트
     */
    @PostMapping("/edit")
    public String editNickname(@RequestParam("nickname") String nickname) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userManagementService.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setNickname(nickname);
                userManagementService.save(user.convertToEntity());
            } else {
                return "redirect:/error";
            }
        }
        return "redirect:/user/mypages";
    }

    /**
     * 사용자가 프로필 사진을 변경하는 메서드.
     *
     * @param filePath 사용자가 업로드한 프로필 이미지 파일
     * @return 변경 완료 후 정보 수정 페이지로 리다이렉트
     */
    @PostMapping("/imageChange")
    public String imageChange(@RequestParam("filePath") MultipartFile filePath) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            try {
                userProfileImageService.changeUserProfileImage(email, filePath);
                return "redirect:/user/edit";
            } catch (Exception e) {
                return "redirect:/user/edit";
            }
        }
        return "redirect:/user/edit";
    }

    /**
     * 마이페이지를 통해 자신이 작성한 레시피, 좋아요한 기본 칵테일, 좋아요한 커스텀 레시피를 조회하는 메서드.
     *
     * @param sort 조회하고자 하는 항목 (myWrite, likeCraft, likeBase)
     * @param model 조회된 레시피 정보를 담는 모델 객체
     * @return 조회된 정보를 담은 레시피 관리 페이지로 이동
     */
    @GetMapping("/recipeManage")
    public String recipeManage(@RequestParam(value = "sort", defaultValue = "myWrite") String sort, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<User> userOptional = userManagementService.findByEmail(email);

        List<CraftCocktailEntity> craftCocktails;
        List<Cocktail> baseCocktails;

        switch (sort) {
            case "likeCraft":
                craftCocktails = likeService.findCraftCocktailsByUserId(userOptional.get().getId());
                model.addAttribute("craftCocktails", craftCocktails);
                break;
            case "likeBase":
                List<LikeEntity> likeCocktails = likeService.findLikeEntityByUserId(userOptional.get().getId());
                List<ObjectId> ids = likeCocktails.stream()
                        .map(LikeEntity::getBasecocktailsId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                baseCocktails = cocktailService.getCocktailsById(ids);
                model.addAttribute("baseCocktails", baseCocktails);
                break;
            default:
                craftCocktails = craftcocktailService.getCocktailsByUserId(userOptional.get().getId());
                model.addAttribute("writeCraftCocktails", craftCocktails);
                break;
        }
        return "user/recipe-manage-view";
    }
}

