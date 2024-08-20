package alcoholboot.toastit.feature.categorysearch.controller;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
//@RestController
@RequiredArgsConstructor
@RequestMapping("/cocktails")
public class CocktailController {
    private final CocktailService cocktailService;

    // 레시피 탐색
    /*@GetMapping("/all")
    public ResponseEntity<Page<Cocktail>> getAllCocktails(@RequestParam(defaultValue = "0") int page) {

        Page<Cocktail> cocktails = cocktailService.getAllCocktailsPaged(PageRequest.of(page, 20)); // 20개씩 페이징

        return ResponseEntity.ok(cocktails);
    }*/
    @GetMapping("/all")
    public String getAllCocktails(
            @RequestParam(defaultValue = "0") int page,
            Model model) {

        Page<Cocktail> cocktails = cocktailService.getAllCocktailsPaged(PageRequest.of(page, 20)); // 20개씩 페이징
        model.addAttribute("cocktails", cocktails);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cocktails.getTotalPages());
        return "/feature/categorysearch/cocktailList";
    }

    /*@GetMapping("/all/ingredient")
    public ResponseEntity<List<Cocktail>> getCocktailsByIngredient(
            @RequestParam String ingredient) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByIngredient(ingredient);
        return ResponseEntity.ok(cocktails);
    }*/
    @GetMapping("/all/ingredient")
    public String getCocktailsByIngredient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String ingredient,
            Model model) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByIngredient(ingredient);

        model.addAttribute("cocktails", cocktails);
        model.addAttribute("page", page);
        model.addAttribute("ingredient", ingredient);
        return "feature/categorysearch/cocktailIngredient";
    }


    //    @GetMapping("/all/glass")
//    public ResponseEntity<List<Cocktail>> getCocktailsByGlass(
//            @RequestParam String glass) {
//        List<Cocktail> cocktails = cocktailService.getCocktailsByGlass(glass);
//        return ResponseEntity.ok(cocktails);
//    }
    @GetMapping("/all/glass")
    public String getCocktailsByGlass(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String glass,
            Model model) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByGlass(glass);

        model.addAttribute("cocktails", cocktails);
        model.addAttribute("page", page);
        model.addAttribute("glass", glass);
        return "feature/categorysearch/cocktailGlass";
    }


    //    @GetMapping("/all/type")
//    public ResponseEntity<List<Cocktail>> getCocktailsByType(
//            @RequestParam String type) {
//        List<Cocktail> cocktails = cocktailService.getCocktailsByType(type);
//        return ResponseEntity.ok(cocktails);
//    }
    @GetMapping("/all/type")
    public String getCocktailsByType(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam String type,
            Model model) {
        List<Cocktail> cocktails = cocktailService.getCocktailsByType(type);

        model.addAttribute("cocktails", cocktails);
        model.addAttribute("page", page);
        model.addAttribute("type", type);
        return "feature/categorysearch/cocktailType";
    }

    /*@GetMapping("/all/mult")
    public ResponseEntity<List<Cocktail>> getCocktailsByMult(
            @RequestParam(required = false) String ingredient,
            @RequestParam(required = false) String glass,
            @RequestParam(required = false) String type,
            Model model){
        List<Cocktail> cocktails = cocktailService.getCocktailsByFilter(ingredient, glass, type);
        model.addAttribute("cocktails", cocktails);
        return ResponseEntity.ok(cocktails);
    }*/
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

        return "feature/categorysearch/cocktailComplex";
    }

    //    @GetMapping("/{id}")
//    public ResponseEntity<Optional<Cocktail>> getCocktailById(
//            @PathVariable String id) {
//
//        Optional<Cocktail> cocktails = cocktailService.getCocktailById(new ObjectId(id));
//        return ResponseEntity.ok(cocktails);
//    }
    @GetMapping("/id")
    public String getCocktailById(
            @RequestParam("id") String id,
            Model model) {
        Optional<Cocktail> cocktail = cocktailService.getCocktailById(new ObjectId(id));
        model.addAttribute("cocktail", cocktail);

        return "feature/categorysearch/cocktailDetails";
    }
}
