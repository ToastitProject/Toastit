package alcoholboot.toastit.feature.customcocktail.controller;

import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.amazonimage.service.S3imageUploadService;
import alcoholboot.toastit.feature.customcocktail.domain.CustomCocktail;
import alcoholboot.toastit.feature.customcocktail.domain.Ingredient;
import alcoholboot.toastit.feature.customcocktail.dto.CocktailDTO;
import alcoholboot.toastit.feature.customcocktail.service.CustomCocktailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/test")
public class CustomCocktailController {

    private static final Logger log = LoggerFactory.getLogger(CustomCocktailController.class);
    private final CustomCocktailService customCocktailService;
    private final S3imageUploadService s3imageUploadService;

    @Autowired
    public CustomCocktailController(CustomCocktailService customCocktailService, S3imageUploadService s3imageUploadService) {
        this.customCocktailService = customCocktailService;
        this.s3imageUploadService = s3imageUploadService;
    }

    @GetMapping("/custom")
    public String customPage(Model model) {
        List<CustomCocktail> cocktails = customCocktailService.getAllCocktails();
        model.addAttribute("cocktails", cocktails);
        return "/custom/custommain";
    }

    @GetMapping("/custom/write")
    public String customWritePage() {
        return "/custom/write";
    }

    @PostMapping("/custom")
    public String saveCocktail(@ModelAttribute CocktailDTO cocktailDTO) {
        try {
            // 이미지 업로드
            String imageUrl = s3imageUploadService.uploadCustomImage(cocktailDTO.getImage());
            log.info("AWS S3에 이미지 업로드 성공: " + imageUrl);

            // 칵테일 정보 저장
            CustomCocktail customCocktail = new CustomCocktail();
            customCocktail.setName(cocktailDTO.getName());
            customCocktail.setDescription(cocktailDTO.getDescription());
            customCocktail.setRecipe(cocktailDTO.getRecipe());

            // 이미지 정보 저장
            Image image = new Image();
            image.setImageName(cocktailDTO.getImage().getOriginalFilename());
            String newUrl = imageUrl.replace("https://s3.amazonaws.com/toastitbucket",
                    "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");
            image.setImagePath(newUrl);
            log.info("MySQL image 에 저장성공");
            image.setImageType(cocktailDTO.getImage().getContentType());
            image.setImageSize(String.valueOf(cocktailDTO.getImage().getSize()));
            customCocktail.getImages().add(image);
            image.setCocktail(customCocktail);

            // 재료 정보 저장
            for (CocktailDTO.IngredientDTO ingredientDTO : cocktailDTO.getIngredients()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientDTO.getName());
                ingredient.setAmount(ingredientDTO.getAmount());
                ingredient.setUnit(ingredientDTO.getUnit());
                ingredient.setCocktail(customCocktail);
                customCocktail.getIngredients().add(ingredient);
            }

            // 칵테일 저장
            customCocktailService.saveCocktail(customCocktail);
            log.info("칵테일 저장 성공: {}", cocktailDTO.getName());

        } catch (IOException e) {
            log.error("이미지 업로드 실패: ", e);
        }

        return "redirect:/test/custom";
    }

    //게시물 세부 페이지
    @GetMapping("/custom/{id}")
    public String showCustomDetail(@PathVariable("id") Long id, Model model) {
        CustomCocktail cocktail = customCocktailService.getCocktailById(id);
        System.out.println(cocktail);
        model.addAttribute("cocktail", cocktail);
        return "/custom/customdetail";
    }
}
