package alcoholboot.toastit.feature.craftcocktail.controller;

import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.amazonimage.service.S3imageUploadService;
import alcoholboot.toastit.feature.craftcocktail.domain.CraftCocktail;
import alcoholboot.toastit.feature.craftcocktail.domain.Ingredient;
import alcoholboot.toastit.feature.craftcocktail.dto.CocktailDTO;
import alcoholboot.toastit.feature.craftcocktail.service.CraftCocktailService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.service.LikeService;
import alcoholboot.toastit.feature.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class CraftCocktailController {

    private static final Logger log = LoggerFactory.getLogger(CraftCocktailController.class);
    private final CraftCocktailService customCocktailService;
    private final S3imageUploadService s3imageUploadService;
    private final UserService userService;
    private final LikeService likeService;

    @Autowired
    public CraftCocktailController(CraftCocktailService customCocktailService, S3imageUploadService s3imageUploadService, UserService userService, LikeService likeService) {
        this.customCocktailService = customCocktailService;
        this.s3imageUploadService = s3imageUploadService;
        this.userService = userService;
        this.likeService = likeService;
    }

    @GetMapping("/craft")
    public String customPage(Model model) {
        List<CraftCocktail> cocktails = customCocktailService.getAllCocktails();
        model.addAttribute("cocktails", cocktails);
        log.info("Accessed custom cocktails page");
        return "feature/craftcocktail/craftmain";
    }

    @GetMapping("/craft/write")
    public String customWritePage(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다");
            return "redirect:/login";
        }
        return "feature/craftcocktail/write";
    }

    @PostMapping("/craft")
    public String saveCocktail(@ModelAttribute CocktailDTO cocktailDTO, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserEntity userEntity = user.convertToEntity();  // User를 UserEntity로 변환

                CraftCocktail customCocktail = new CraftCocktail();
                customCocktail.setName(cocktailDTO.getName());
                customCocktail.setDescription(cocktailDTO.getDescription());
                customCocktail.setRecipe(cocktailDTO.getRecipe());
                customCocktail.setUser(userEntity);  // 변환된 UserEntity 설정

                // 재료 추가
                for (CocktailDTO.IngredientDTO ingredientDTO : cocktailDTO.getIngredients()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(ingredientDTO.getName());
                    ingredient.setAmount(ingredientDTO.getAmount());
                    ingredient.setUnit(ingredientDTO.getUnit());
                    customCocktail.addIngredient(ingredient);
                }

                // 이미지 추가 및 업로드
                if (cocktailDTO.getImage() != null && !cocktailDTO.getImage().isEmpty()) {
                    String imagePath = s3imageUploadService.uploadCustomImage(cocktailDTO.getImage());
                    String newUrl = imagePath.replace("https://s3.amazonaws.com/toastitbucket",
                            "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");
                    Image image = new Image();
                    image.setImageName(cocktailDTO.getImage().getOriginalFilename());
                    image.setImagePath(newUrl);
                    image.setUser(userEntity);
                    image.setCocktail(customCocktail);
                    customCocktail.getImages().add(image);
                }

                customCocktailService.saveCocktail(customCocktail);
                log.info("칵테일 저장 성공: {}", cocktailDTO.getName());
            } else {
                log.warn("유저를 찾을 수 없습니다: " + email);
                redirectAttributes.addFlashAttribute("message", "유저 정보를 찾을 수 없습니다.");
                return "redirect:/login";
            }
        } catch (Exception e) {
            log.error("칵테일 저장 실패: ", e);
            redirectAttributes.addFlashAttribute("message", "칵테일 저장 중 오류가 발생했습니다.");
            return "redirect:/craft/write";
        }

        return "redirect:/craft";
    }

    @GetMapping("/craft/edit/{id}")
    public String editCocktailForm(@PathVariable("id") Long id, Model model) {
        CraftCocktail cocktail = customCocktailService.getCocktailById(id);
        model.addAttribute("cocktail", cocktail);
        model.addAttribute("image", cocktail.getImages());
        model.addAttribute("ingredients", cocktail.getIngredients()); // Add ingredients to the model
        return "feature/craftcocktail/edit";
    }

    @PostMapping("/craft/edit/{id}")
    public String updateCocktail(@PathVariable("id") Long id, @ModelAttribute CocktailDTO cocktailDTO, RedirectAttributes redirectAttributes) {
        try {
            CraftCocktail existingCocktail = customCocktailService.getCocktailById(id);

            // 칵테일 정보 업데이트
            existingCocktail.setName(cocktailDTO.getName());
            existingCocktail.setDescription(cocktailDTO.getDescription());
            existingCocktail.setRecipe(cocktailDTO.getRecipe());

            // 이미지 업데이트 제어
            if (cocktailDTO.getImage() != null && !cocktailDTO.getImage().isEmpty()) {
                String imagePath = s3imageUploadService.uploadCustomImage(cocktailDTO.getImage());
                String newUrl = imagePath.replace("https://s3.amazonaws.com/toastitbucket",
                        "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");

                Image existingImage = existingCocktail.getImages().stream()
                        .filter(img -> img.getImageName().equals(cocktailDTO.getImage().getOriginalFilename()))
                        .findFirst()
                        .orElse(null);

                if (existingImage != null) {
                    // 기존 이미지 업데이트
                    existingImage.setImagePath(newUrl);
                } else {
                    // 새로운 이미지 추가
                    Image newImage = new Image();
                    newImage.setImageName(cocktailDTO.getImage().getOriginalFilename());
                    newImage.setImagePath(newUrl);
                    newImage.setCocktail(existingCocktail);

                    existingCocktail.getImages().add(newImage);
                }
            }

            // 칵테일 저장
            customCocktailService.saveCocktail(existingCocktail);
            redirectAttributes.addFlashAttribute("message", "칵테일이 성공적으로 수정되었습니다.");
            return "redirect:/craft/" + id;
        } catch (Exception e) {
            log.error("칵테일 수정 실패: ", e);
            redirectAttributes.addFlashAttribute("message", "칵테일 수정 중 오류가 발생했습니다.");
            return "redirect:/craft/edit/" + id;
        }
    }

    @PostMapping("/craft/delete/{id}")
    public String deleteCocktail(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            //유저 아이디와 커스텀 칵테일 아이디로 좋아요 객체를 찾아서 지운다
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loginUserEmail = authentication.getName();
            Optional<User> loginUser = userService.findByEmail(loginUserEmail);

            Optional<LikeEntity> deleteLikeOpt = Optional.ofNullable(likeService.findByUserIdAndCustomCocktailId(loginUser.get().getId(), id));
            deleteLikeOpt.ifPresent(likeService::deleteLike); // 좋아요가 있는 경우에만 삭제

            //칵테일도 지운다
            customCocktailService.deleteCocktail(id);
            redirectAttributes.addFlashAttribute("message", "칵테일이 성공적으로 삭제되었습니다.");
            return "redirect:/craft";
        } catch (Exception e) {
            log.error("칵테일 삭제 실패: ", e);
            redirectAttributes.addFlashAttribute("message", "칵테일 삭제 중 오류가 발생했습니다.");
            return "redirect:/craft/" + id;
        }
    }

    @GetMapping("/craft/{id}")
    public String showCustomDetail(@PathVariable("id") Long id, Model model) {
        CraftCocktail cocktail = customCocktailService.getCocktailById(id);
        model.addAttribute("cocktail", cocktail);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String loginUserEmail = authentication.getName();
            Optional<User> loginUser = userService.findByEmail(loginUserEmail);

            if (loginUser.isPresent()) {
                User user = loginUser.get();
                boolean isOwner = cocktail.getUser().getId().equals(user.getId());
                model.addAttribute("isOwner", isOwner);

                LikeEntity existingLike = likeService.findByUserIdAndCustomCocktailId(user.getId(), id);
                model.addAttribute("isLiked", existingLike != null);
            } else {
                model.addAttribute("isOwner", false);
                model.addAttribute("isLiked", false);
            }
        } else {
            model.addAttribute("isOwner", false);
            model.addAttribute("isLiked", false);
        }

        return "feature/craftcocktail/craftdetail";
    }

}
