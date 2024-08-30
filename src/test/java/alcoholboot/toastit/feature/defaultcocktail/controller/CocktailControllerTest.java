//package alcoholboot.toastit.feature.defaultcocktail.controller;
//
//import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailDocument;
//import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailEntity;
//import alcoholboot.toastit.feature.defaultcocktail.AWSService.CocktailService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.*;
//
//class CocktailControllerTest {
//
//    @Mock
//    private CocktailService cocktailService;
//
//    @InjectMocks
//    private CocktailController cocktailController;
//
//    private CocktailEntity cocktail1;
//    private CocktailEntity cocktail2;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        cocktail1 = new CocktailEntity();
//        cocktail1.setStrDrink("Margarita");
//        cocktail1.setStrCategory("Ordinary Drink");
//        cocktail1.setStrGlass("Cocktail glass");
//        cocktail1.setStrIngredients(Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt"));
//
//        cocktail2 = new CocktailEntity();
//        cocktail2.setStrDrink("Mojito");
//        cocktail2.setStrCategory("Cocktail");
//        cocktail2.setStrGlass("Highball glass");
//        cocktail2.setStrIngredients(Arrays.asList("White rum", "Sugar", "Lime juice", "Soda water", "Mint"));
//    }
//
//    @Test
//    void getAllCocktails() {
//        Page<CocktailEntity> page = new PageImpl<>(Arrays.asList(cocktail1, cocktail2));
//        when(cocktailService.getAllCocktailsPaged(any(PageRequest.class))).thenReturn(page);
//
//        ResponseEntity<List<CocktailEntity>> response = cocktailController.getAllCocktails(null, null, null, 0);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size());
//        assertEquals("Margarita", response.getBody().get(0).getStrDrink());
//        assertEquals("Mojito", response.getBody().get(1).getStrDrink());
//    }
//
//    @Test
//    void getCocktailsByFilter() {
////        when(cocktailService.getCocktailsByFilter("Tequila", "Cocktail glass", "Ordinary Drink"))
////                .thenReturn(Arrays.asList(cocktail1));
////
////        ResponseEntity<List<CocktailEntity>> response = cocktailController.getAllCocktails("Tequila", "Cocktail glass", "Ordinary Drink", 0);
////
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals(1, response.getBody().size());
////        assertEquals("Margarita", response.getBody().get(0).getStrDrink());
//    }
//
//    @Test
//    void getCocktailDetails() {
//        when(cocktailService.getCocktailById("1")).thenReturn(Optional.of(cocktail1));
//
////        ResponseEntity<CocktailDocument> response = cocktailController.getCocktailDetails("1");
//
////        assertEquals(HttpStatus.OK, response.getStatusCode());
////        assertEquals("Margarita", response.getBody().getStrDrink());
//    }
//
//    @Test
//    void getCocktailDetailsNotFound() {
////        when(cocktailService.getCocktailById("999")).thenReturn(Optional.empty());
////
////        ResponseEntity<CocktailEntity> response = cocktailController.getCocktailDetails("999");
////
////        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//    }
//}