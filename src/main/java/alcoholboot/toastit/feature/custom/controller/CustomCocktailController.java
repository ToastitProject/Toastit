package alcoholboot.toastit.feature.custom.controller;

import alcoholboot.toastit.feature.amazonimage.service.S3imageUploadService;
import alcoholboot.toastit.feature.custom.domain.CustomCocktail;
import alcoholboot.toastit.feature.custom.domain.Ingredient;
import alcoholboot.toastit.feature.custom.service.CustomCocktailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/test")
public class CustomCocktailController {

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

    @PostMapping("/save")
    public String saveCocktail(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("recipe") String recipe,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam List<String> ingredientName,
            @RequestParam List<String> amount,
            @RequestParam List<String> unit
    ) {
        CustomCocktail cocktail = new CustomCocktail();
        cocktail.setName(name);
        cocktail.setDescription(description);
        cocktail.setRecipe(recipe);

        // 이미지 업로드 처리
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                imageUrl = s3imageUploadService.uploadCustomImage(imageFile);
            } catch (IOException e) {
                e.printStackTrace(); // 예외 처리
            }
        }
        cocktail.setImageUrl(imageUrl); // 이미지 URL 설정

        List<Ingredient> ingredients = new ArrayList<>();
        for (int i = 0; i < ingredientName.size(); i++) {
            Ingredient ingredient = new Ingredient();
            ingredient.setName(ingredientName.get(i));
            ingredient.setAmount(amount.get(i));
            ingredient.setUnit(unit.get(i));
            ingredients.add(ingredient);
        }

        // 메서드 호출 시 올바른 파라미터 전달
        customCocktailService.saveCocktail(cocktail, imageFile, ingredients);
        return "redirect:/test/custom";
    }



}
