package alcoholboot.toastit.feature.defaultcocktail.controller;

import alcoholboot.toastit.feature.defaultcocktail.domain.Cocktail;
import alcoholboot.toastit.feature.defaultcocktail.service.CocktailService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cocktails")
public class CocktailController {
    private final CocktailService cocktailService;

    @GetMapping("/all")
    public String getAllCocktails(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Cocktail> cocktails = cocktailService.getAllCocktailsPaged(PageRequest.of(page, 20)); // 20개씩 페이징
        model.addAttribute("cocktails", cocktails);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cocktails.getTotalPages());
        return "/feature/defaultcocktail/cocktailList";
    }

    @GetMapping("/all/ingredient")
    public String getCocktailsByIngredient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String ingredient,
            Model model) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByIngredient(ingredient);

        model.addAttribute("cocktails", cocktails);
        model.addAttribute("page", page);
        model.addAttribute("ingredient", ingredient);
        return "feature/defaultcocktail/cocktailIngredient";
    }

    @GetMapping("/all/glass")
    public String getCocktailsByGlass(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String glass,
            Model model) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByGlass(glass);

        model.addAttribute("cocktails", cocktails);
        model.addAttribute("page", page);
        model.addAttribute("glass", glass);
        return "feature/defaultcocktail/cocktailGlass";
    }

    @GetMapping("/all/type")
    public String getCocktailsByType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String type,
            Model model) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByType(type);

        model.addAttribute("cocktails", cocktails);
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        return "feature/defaultcocktail/cocktailType";
    }

    @GetMapping("/all/mult")
    public String getCocktailsByMult(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String glass,
            @RequestParam(required = false) String type,
            Model model) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByFilter(ingredient, glass, type);
        model.addAttribute("page", page);
        model.addAttribute("cocktails", cocktails);
        model.addAttribute("ingredient", ingredient);
        model.addAttribute("glass", glass);
        model.addAttribute("type", type);

        return "feature/defaultcocktail/cocktailComplex";
    }

    @GetMapping("/id")
    public String getCocktailById(
            @RequestParam("id") String id,
            Model model) {
        Optional<Cocktail> cocktail = cocktailService.getCocktailById(new ObjectId(id));
        model.addAttribute("cocktail", cocktail);

        return "feature/defaultcocktail/cocktailDetails";
    }
}
