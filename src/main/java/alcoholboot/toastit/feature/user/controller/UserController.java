package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;
import alcoholboot.toastit.feature.basecocktail.service.CocktailService;
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
     * 메인 화면에서 마이페이지를 누르면 동작하는 메서드 입니다.
     * @param model : 로그인 한 사용자의 정보를 담고 있습니다.
     * @return : 내 정보를 볼 수 있는 페이지로 이동합니다.
     */
    @GetMapping("/mypages")
    public String showMyPages(Model model) {
        //인증된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            //접속한 사용자를 이메일로 찾아온다
            Optional<User> userOptional = userManagementService.findByEmail(email);
            if (userOptional.isPresent()) {
                //접속한 사용자의 정보를 보내준다
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
     * 닉네임을 클릭하여 자신의 정보 또는 다른 사용자의 정보를 볼 수 있는 페이지로 연결해주는 메서드 입니다.
     * @param nickname : 정보를 조회 하고 싶은 사용자의 닉네임 입니다
     * @param model : 정보를 조회하고 싶은 사용자의 정보입니다.
     * @return : 사용자의 정보를 볼 수 있는 페이지로 이동합니다.
     */
    @GetMapping("/mypage")
    public String showMyPage(@RequestParam("nickname") String nickname, Model model) {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();

            // 로그인한 사용자 정보 조회
            Optional<User> loggedInUserOptional = userManagementService.findByEmail(email);
            if (loggedInUserOptional.isPresent()) {
                User loggedInUser = loggedInUserOptional.get();
                model.addAttribute("loggedInUser", loggedInUser);

                // 요청된 닉네임과 로그인한 사용자의 닉네임 비교
                if (loggedInUser.getNickname().equals(nickname)) {
                    // 자신의 정보인 경우
                    model.addAttribute("user", loggedInUser);
                    model.addAttribute("notLoginUser", false);

                } else {
                    // 다른 사용자의 정보인 경우
                    Optional<User> otherUserOptional = userManagementService.findByNickname(nickname);

                    //접속한 user id와 팔로우할 사람의 id로 조회하여 FollowEntity를 생성합니다.
                    FollowEntity alreadyFollow = followService.findByFollowerIdAndFolloweeId(
                            loggedInUser.getId(),
                            otherUserOptional.get().getId()
                    );
                    if (otherUserOptional.isPresent()) {
                        model.addAttribute("user", otherUserOptional.get());
                        model.addAttribute("notLoginUser", true);

                        if (alreadyFollow != null) {
                            //팔로우를 하고 있다면 모델에 alreadyFollow를 담아갑니다.
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
            model.addAttribute("error", "사용자가 인증되지 않았습니다.");
        }

        return "user/mypage-form";
    }

    /**
     * 사용자가 자신의 정보를 수정하기 위한 페이지로 이동하는 메서드 입니다
     * @param model : 현재 접속한 사용자의 정보를 담고 있습니다.
     * @return 회원 정보 수정이 가능한 페이지로 이동합니다.
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
     * 사용자가 정보를 변경 후 수정을 완료하기 위해 보내는 요청에 응답하는 메서드 입니다.
     * @param nickname : 사용자가 변경을 원하는 닉네임 입니다
     * @return : 변경을 완료하면 mypage 화면으로 디라이렉션 합니다
     */
    @PostMapping("/edit")
    public String editNickname(@RequestParam("nickname") String nickname) {

        //인증된 사용자 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userManagementService.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // 닉네임 변경
                user.setNickname(nickname);
                userManagementService.save(user.convertToEntity()); // 변경된 사용자 정보 저장

            } else {
                return "redirect:/error"; // 사용자 없음 처리
            }
        }

        return "redirect:/user/mypages"; // 변경 후 마이 페이지로 리다이렉트
    }

    /**
     * 프로필 사진의 이미지를 변경하고자 하는 요청이 들어왔을 때 실행되는 메서드 입니다.
     * @param filePath : 사용자가 올린 이미지의 정보를 받아옵니다.
     * @return : 이미지 변경이 성공되면, 정보수정 페이지로 리다이렉션 합니다
     */
    @PostMapping("/imageChange")
    public String imageChange(@RequestParam("filePath") MultipartFile filePath) {
        //인증된 사용자 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            try {
                //새로운 이미지로 변경
                userProfileImageService.changeUserProfileImage(email, filePath);
                return "redirect:/user/edit";
            } catch (Exception e) {
                return "redirect:/user/edit";
            }
        }
        return "redirect:/user/edit";
    }

    /**
     * 마이페이지를 통해 자신이 작성한 레시피, 좋아요 한 기본 칵테일, 좋아요한 커스텀 레시피를 조회할 수 있는 기능입니다.
     * @param sort :  서버로 부터 조회하고자 하는 값 중 하나를 받아옵니다. ( default 값은 작성한 레시피 입니다.)
     * @param model :  view 에 보여줄 칵테일을 담습니다.
     * @return : 사용자의 요청에 맞는 칵테일 정보들을 view 로 전달해줍니다
     */
    @GetMapping("/recipeManage")
    public String recipeManage(@RequestParam(value = "sort", defaultValue = "myWrite") String sort, Model model) {
        //인증된 사용자 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Optional<User> userOptional = userManagementService.findByEmail(email);

        //칵테일을 담을 자료구조 선언
        List<CraftCocktailEntity> craftCocktails;
        List<Cocktail> baseCocktails;

        switch (sort) {
            //커스텀 레시피를 조회하는 요청
            case "likeCraft":
                craftCocktails = likeService.findCraftCocktailsByUserId(userOptional.get().getId());
                model.addAttribute("craftCocktails", craftCocktails);
                break;
            //기본 칵테일을 조회하는 요청
            case "likeBase":
                List<LikeEntity> likeCocktails = likeService.findLikeEntityByUserId(userOptional.get().getId());
                List<ObjectId> ids = likeCocktails.stream()
                        .map(LikeEntity::getBasecocktailsId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                baseCocktails = cocktailService.getCocktailsById(ids);
                model.addAttribute("baseCocktails", baseCocktails);
                break;
            //내가 작성한 커스텀 레시피를 조회하는 요청
            default:
                craftCocktails = craftcocktailService.getCocktailsByUserId(userOptional.get().getId());
                model.addAttribute("writeCraftCocktails", craftCocktails);
                break;
        }
        return "user/recipe-manage-view";
    }
}

