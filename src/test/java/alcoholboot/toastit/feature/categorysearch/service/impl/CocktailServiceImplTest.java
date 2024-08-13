package alcoholboot.toastit.feature.categorysearch.service.impl;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.repository.CocktailRepository;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class CocktailServiceImplTest {

    @Autowired
    private CocktailService cocktailService;

    @Autowired
    private CocktailRepository cocktailRepository;

//    @Test
//    void getAllCocktails() {
//    }
//
//    @Test
//    void getAllCocktailsPaged() {
//    }
//
//    @Test
//    void getCocktailsByIngredient() {
//    }
//
//    @Test
//    void getCocktailsByGlass() {
//    }
//
//    @Test
//    void getCocktailsByType() {
//    }
//
//    @Test
//    void getCocktailsByFilter() {
//    }

    @Test
    void getCocktailById() {
        ObjectId id = new ObjectId("66b4134b6f4b8bfc07fde4dd");

        Optional<Cocktail> cocktailOptional = cocktailService.getCocktailById(id);

        if(cocktailOptional.isPresent()) {
            Cocktail cocktail = cocktailOptional.get();

            log.info(cocktail.toString());
        } else {
            log.error("칵테일 없어요 ~~~~~~~~~~~~~~~~~~~~~~₩");
        }
    }
}