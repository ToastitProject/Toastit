package alcoholboot.toastit.feature.categorysearch.controller;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cocktails")
public class CocktailController {
    private final CocktailService cocktailService;

    // 레시피 탐색, 카테고리 적용 가능
    @GetMapping("/all")
    public ResponseEntity<Page<Cocktail>> getAllCocktails(@RequestParam(defaultValue = "0") int page) {

        Page<Cocktail> cocktails = cocktailService.getAllCocktailsPaged(PageRequest.of(page, 20)); // 20개씩 페이징

        return ResponseEntity.ok(cocktails);
    }

//    // 레시피 상세 페이지
//    @GetMapping("/{id}")
//    public ResponseEntity<CocktailEntity> getCocktailDetails(@PathVariable String id) {
//        return cocktailService.getCocktailById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
}
