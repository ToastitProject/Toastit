package alcoholboot.toastit.feature.custom.controller;

import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.amazonimage.service.S3imageUploadService;
import alcoholboot.toastit.feature.custom.domain.CustomCocktail;
import alcoholboot.toastit.feature.custom.domain.Ingredient;
import alcoholboot.toastit.feature.custom.service.CustomCocktailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public String saveCocktail(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("recipe") String recipe,
            @RequestParam("image") MultipartFile filePath,
            @RequestParam("ingredients[0].name") List<String> ingredientName,
            @RequestParam("ingredients[0].amount") List<String> amount,
            @RequestParam("ingredients[0].unit") List<String> unit
    ) {
        try {
            // 이미지 업로드
            String imageUrl = s3imageUploadService.uploadImage(filePath);
            log.info("AWS S3에 이미지 업로드 성공: " + imageUrl);

            // 칵테일 정보 저장
            CustomCocktail customCocktail = new CustomCocktail();
            customCocktail.setName(name);
            customCocktail.setDescription(description);
            customCocktail.setRecipe(recipe);

            // 이미지 정보 저장
            Image image = new Image();
            image.setImageName(filePath.getOriginalFilename());
            image.setImagePath(imageUrl);
            image.setImageType(filePath.getContentType());
            image.setImageSize(String.valueOf(filePath.getSize()));
            customCocktail.getImages().add(image);
            image.setCocktail(customCocktail);

            // 재료 정보 저장
            for (int i = 0; i < ingredientName.size(); i++) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(ingredientName.get(i));
                ingredient.setAmount(amount.get(i));
                ingredient.setUnit(unit.get(i));
                ingredient.setCocktail(customCocktail);
                customCocktail.getIngredients().add(ingredient);
            }

            // 칵테일 저장
            customCocktailService.saveCocktail(customCocktail);
            log.info("칵테일 저장 성공: {}", name);

        } catch (IOException e) {
            log.error("이미지 업로드 실패: ", e);
        }

        return "redirect:/test/custom";
    }

}
