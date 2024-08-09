package alcoholboot.toastit.feature.categorysearch.controller;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/cocktails")
public class CocktailController {
    private final CocktailService cocktailService;

    // 레시피 탐색, 카테고리 적용 가능
    @GetMapping("/all")
    public String getAllCocktails(
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String glass,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Cocktail> cocktailPage;
        if (ingredient != null || glass != null || category != null) {
            cocktailPage = cocktailService.getCocktailsByMultipleFilters(ingredient, glass, category, PageRequest.of(page, size));
        } else {
            cocktailPage = cocktailService.getAllCocktails(PageRequest.of(page, size));
        }

        model.addAttribute("cocktails", cocktailPage.getContent());
        model.addAttribute("currentPage", cocktailPage.getNumber());
        model.addAttribute("totalPages", cocktailPage.getTotalPages());
        return "cocktailList";
    }

    // 레시피 상세 페이지
    @GetMapping("/{id}")
    public String getCocktailDetails(@PathVariable String id, Model model) {
        Cocktail cocktail = cocktailService.getCocktailById(id);
        model.addAttribute("cocktail", cocktail);
        return "cocktailDetails";
    }
}