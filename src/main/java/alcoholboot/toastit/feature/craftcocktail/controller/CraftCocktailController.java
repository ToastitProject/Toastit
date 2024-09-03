package alcoholboot.toastit.feature.craftcocktail.controller;

import alcoholboot.toastit.feature.craftcocktail.service.CraftCocktailService;
import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.image.service.CloudStorageService;
import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.craftcocktail.entity.IngredientEntity;
import alcoholboot.toastit.feature.craftcocktail.controller.request.CraftCocktailCreateRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.service.LikeService;
import alcoholboot.toastit.feature.user.service.UserManagementService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CraftCocktailController {

    private final CraftCocktailService customCocktailService;
    private final CloudStorageService cloudStorageService;
    private final UserManagementService userManagementService;
    private final LikeService likeService;

    @GetMapping("/craft")
    public String customPage(Model model) {
        List<CraftCocktailEntity> cocktails = customCocktailService.getAllCocktails();
        model.addAttribute("cocktails", cocktails);
        log.debug("Accessed custom cocktails page");
        return "craftcocktail/craftmain";
    }

    @GetMapping("/craft/write")
    public String customWritePage(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다");
            return "redirect:/auth/login";
        }

        return "craftcocktail/write";
    }

    @PostMapping("/craft")
    public String saveCocktail(@ModelAttribute CraftCocktailCreateRequest craftCocktailCreateRequest, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
            return "redirect:/auth/login";
        }

        try {
            String email = authentication.getName();
            Optional<User> userOptional = userManagementService.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                CraftCocktailEntity craftCocktail = new CraftCocktailEntity();
                craftCocktail.setName(craftCocktailCreateRequest.getName());
                craftCocktail.setDescription(craftCocktailCreateRequest.getDescription());
                craftCocktail.setRecipe(craftCocktailCreateRequest.getRecipe());
                craftCocktail.setUser(user.convertToEntity());  // 변환된 UserEntity 설정

                // 재료 추가
                for (CraftCocktailCreateRequest.IngredientDTO ingredientDTO : craftCocktailCreateRequest.getIngredients()) {
                    IngredientEntity ingredient = new IngredientEntity();
                    ingredient.setName(ingredientDTO.getName());
                    ingredient.setAmount(ingredientDTO.getAmount());
                    ingredient.setUnit(ingredientDTO.getUnit());
                    craftCocktail.addIngredient(ingredient);
                }

                // 이미지 추가 및 업로드
                if (craftCocktailCreateRequest.getImage() != null && !craftCocktailCreateRequest.getImage().isEmpty()) {
                    String imagePath = cloudStorageService.uploadCraftCocktailImage(craftCocktailCreateRequest.getImage());
                    String newUrl = imagePath.replace("https://s3.amazonaws.com/toastitbucket",
                            "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");
                    ImageEntity imageEntity = new ImageEntity();
                    imageEntity.setImageName(craftCocktailCreateRequest.getImage().getOriginalFilename());
                    imageEntity.setImagePath(newUrl);
                    imageEntity.setUser(user.convertToEntity());
                    imageEntity.setCocktail(craftCocktail);
                    craftCocktail.getImages().add(imageEntity);
                }

                customCocktailService.saveCocktail(craftCocktail);
                log.info("칵테일 저장 성공: {}", craftCocktailCreateRequest.getName());
            } else {
                log.warn("유저를 찾을 수 없습니다: " + email);
                redirectAttributes.addFlashAttribute("message", "유저 정보를 찾을 수 없습니다.");
                return "redirect:/auth/login";
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
        CraftCocktailEntity cocktail = customCocktailService.getCocktailById(id);
        model.addAttribute("cocktail", cocktail);
        model.addAttribute("image", cocktail.getImages());
        model.addAttribute("ingredients", cocktail.getIngredients()); // Add ingredients to the model
        return "craftcocktail/edit";
    }

    @PostMapping("/craft/edit/{id}")
    public String updateCocktail(@PathVariable("id") Long id, @ModelAttribute CraftCocktailCreateRequest craftCocktailCreateRequest, RedirectAttributes redirectAttributes) {
        try {
            CraftCocktailEntity existingCocktail = customCocktailService.getCocktailById(id);

            // 칵테일 정보 업데이트
            existingCocktail.setName(craftCocktailCreateRequest.getName());
            existingCocktail.setDescription(craftCocktailCreateRequest.getDescription());
            existingCocktail.setRecipe(craftCocktailCreateRequest.getRecipe());

            // 이미지 업데이트 제어
            if (craftCocktailCreateRequest.getImage() != null && !craftCocktailCreateRequest.getImage().isEmpty()) {
                String imagePath = cloudStorageService.uploadCraftCocktailImage(craftCocktailCreateRequest.getImage());
                String newUrl = imagePath.replace("https://s3.amazonaws.com/toastitbucket",
                        "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");

                ImageEntity existingImageEntity = existingCocktail.getImages().stream()
                        .filter(img -> img.getImageName().equals(craftCocktailCreateRequest.getImage().getOriginalFilename()))
                        .findFirst()
                        .orElse(null);

                if (existingImageEntity != null) {
                    // 기존 이미지 업데이트
                    existingImageEntity.setImagePath(newUrl);
                } else {
                    // 새로운 이미지 추가
                    ImageEntity newImageEntity = new ImageEntity();
                    newImageEntity.setImageName(craftCocktailCreateRequest.getImage().getOriginalFilename());
                    newImageEntity.setImagePath(newUrl);
                    newImageEntity.setCocktail(existingCocktail);

                    existingCocktail.getImages().add(newImageEntity);
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
            Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

            Optional<LikeEntity> deleteLikeOpt = Optional.ofNullable(likeService.findByUserIdAndCraftCocktailId(loginUser.get().getId(), id));
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
        CraftCocktailEntity cocktail = customCocktailService.getCocktailById(id);
        model.addAttribute("cocktail", cocktail);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String loginUserEmail = authentication.getName();
            Optional<User> loginUser = userManagementService.findByEmail(loginUserEmail);

            if (loginUser.isPresent()) {
                User user = loginUser.get();
                boolean isOwner = cocktail.getUser().getId().equals(user.getId());
                model.addAttribute("isOwner", isOwner);

                LikeEntity existingLike = likeService.findByUserIdAndCraftCocktailId(user.getId(), id);
                model.addAttribute("isLiked", existingLike != null);
            } else {
                model.addAttribute("isOwner", false);
                model.addAttribute("isLiked", false);
            }
        } else {
            model.addAttribute("isOwner", false);
            model.addAttribute("isLiked", false);
        }

        return "craftcocktail/craftdetail";
    }
}