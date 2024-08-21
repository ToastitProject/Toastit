package alcoholboot.toastit.feature.customcocktail.controller;

import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.amazonimage.service.S3imageUploadService;
import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.customcocktail.domain.Ingredient;
import alcoholboot.toastit.feature.customcocktail.dto.CocktailDTO;
import alcoholboot.toastit.feature.customcocktail.service.CustomCocktailService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.UserEntity;
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

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomCocktailController {

    private static final Logger log = LoggerFactory.getLogger(CustomCocktailController.class);
    private final CustomCocktailService customCocktailService;
    private final S3imageUploadService s3imageUploadService;
    private final UserService userService;

    @Autowired
    public CustomCocktailController(CustomCocktailService customCocktailService, S3imageUploadService s3imageUploadService, UserService userService) {
        this.customCocktailService = customCocktailService;
        this.s3imageUploadService = s3imageUploadService;
        this.userService = userService;
    }

    @GetMapping("/custom")
    public String customPage(Model model) {
        List<CustomCocktail> cocktails = customCocktailService.getAllCocktails();
        model.addAttribute("cocktails", cocktails);
        log.info("Accessed custom cocktails page");
        return "/feature/customcocktail/custommain";
    }

    @GetMapping("/custom/write")
    public String customWritePage(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다");
            return "redirect:/login";
        }
        return "/feature/customcocktail/write";
    }

    @PostMapping("/custom")
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

                CustomCocktail customCocktail = new CustomCocktail();
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
            return "redirect:/custom/write";
        }

        return "redirect:/custom";
    }

    @GetMapping("/custom/edit/{id}")
    public String editCocktailForm(@PathVariable("id") Long id, Model model) {
        CustomCocktail cocktail = customCocktailService.getCocktailById(id);
        model.addAttribute("cocktail", cocktail);
        model.addAttribute("ingredients", cocktail.getIngredients()); // Add ingredients to the model
        return "/feature/customcocktail/edit";
    }

    @PostMapping("/custom/edit/{id}")
    public String updateCocktail(@PathVariable("id") Long id, @ModelAttribute CocktailDTO cocktailDTO, RedirectAttributes redirectAttributes) {
        try {
            CustomCocktail existingCocktail = customCocktailService.getCocktailById(id);

            // Update cocktail details
            existingCocktail.setName(cocktailDTO.getName());
            existingCocktail.setDescription(cocktailDTO.getDescription());
            existingCocktail.setRecipe(cocktailDTO.getRecipe());

            // Clear existing ingredients and add updated ones
            existingCocktail.getIngredients().clear();
            for (CocktailDTO.IngredientDTO ingredientDTO : cocktailDTO.getIngredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientDTO.getName());
                ingredient.setAmount(ingredientDTO.getAmount());
                ingredient.setUnit(ingredientDTO.getUnit());
                existingCocktail.addIngredient(ingredient);
            }

            // Handle image update
            if (cocktailDTO.getImage() != null && !cocktailDTO.getImage().isEmpty()) {
                // Handle image update, ensuring no duplicates
                String imagePath = s3imageUploadService.uploadCustomImage(cocktailDTO.getImage());
                String newUrl = imagePath.replace("https://s3.amazonaws.com/toastitbucket",
                        "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");

                Image newImage = new Image();
                newImage.setImageName(cocktailDTO.getImage().getOriginalFilename());
                newImage.setImagePath(newUrl);
                newImage.setCocktail(existingCocktail);

                // Check if image already exists in the cocktail
                boolean isDuplicate = existingCocktail.getImages().stream()
                        .anyMatch(img -> img.getImageName().equals(newImage.getImageName()) && img.getImagePath().equals(newImage.getImagePath()));

                if (!isDuplicate) {
                    // Optionally clear old images if you want to only keep the latest one
                    existingCocktail.getImages().clear();
                    existingCocktail.getImages().add(newImage);
                } else {
                    log.info("이미지가 중복되어 업데이트하지 않았습니다: {}", newImage.getImageName());
                }
            }

            customCocktailService.saveCocktail(existingCocktail);
            redirectAttributes.addFlashAttribute("message", "칵테일이 성공적으로 수정되었습니다.");
            return "redirect:/custom/" + id;
        } catch (Exception e) {
            log.error("칵테일 수정 실패: ", e);
            redirectAttributes.addFlashAttribute("message", "칵테일 수정 중 오류가 발생했습니다.");
            return "redirect:/custom/edit/" + id;
        }
    }



    @PostMapping("/custom/delete/{id}")
    public String deleteCocktail(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            customCocktailService.deleteCocktail(id);
            redirectAttributes.addFlashAttribute("message", "칵테일이 성공적으로 삭제되었습니다.");
            return "redirect:/custom";
        } catch (Exception e) {
            log.error("칵테일 삭제 실패: ", e);
            redirectAttributes.addFlashAttribute("message", "칵테일 삭제 중 오류가 발생했습니다.");
            return "redirect:/custom/" + id;
        }
    }

    @GetMapping("/custom/{id}")
    public String showCustomDetail(@PathVariable("id") Long id, Model model) {
        CustomCocktail cocktail = customCocktailService.getCocktailById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<User> userOptional = userService.findByEmail(email);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                boolean isOwner = cocktail.getUser().getEmail().equals(user.getEmail());
                model.addAttribute("isOwner", isOwner);
            }
        }

        model.addAttribute("cocktail", cocktail);
        return "/feature/customcocktail/customdetail";
    }
}
