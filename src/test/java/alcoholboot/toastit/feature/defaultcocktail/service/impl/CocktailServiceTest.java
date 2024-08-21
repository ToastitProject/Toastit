/*
package alcoholboot.toastit.feature.defaultcocktail.service.impl;

import alcoholboot.toastit.feature.defaultcocktail.domain.Cocktail;
import alcoholboot.toastit.feature.defaultcocktail.entity.CocktailEntity;
import alcoholboot.toastit.feature.defaultcocktail.repository.CocktailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CocktailServiceTest {

    @Mock
    private CocktailRepository cocktailRepository;

    @InjectMocks
    private CocktailServiceImpl cocktailService;

    private Cocktail cocktail1;
    private Cocktail cocktail2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cocktail1 = new Cocktail();
        cocktail1.setStrDrink("Margarita");
        cocktail1.setStrCategory("Ordinary Drink");
        cocktail1.setStrGlass("Cocktail glass");
        cocktail1.setStrIngredients(Arrays.asList("Tequila", "Triple sec", "Lime juice", "Salt"));

        cocktail2 = new Cocktail();
        cocktail2.setStrDrink("Mojito");
        cocktail2.setStrCategory("Cocktail");
        cocktail2.setStrGlass("Highball glass");
        cocktail2.setStrIngredients(Arrays.asList("White rum", "Sugar", "Lime juice", "Soda water", "Mint"));
    }

    @Test
    void getAllCocktails() {
        when(cocktailRepository.findAll()).thenReturn(Arrays.asList(cocktail1, cocktail2));

        List<CocktailEntity> result = cocktailService.getAllCocktails();

        assertEquals(2, result.size());
        assertEquals("Margarita", result.get(0).getStrDrink());
        assertEquals("Mojito", result.get(1).getStrDrink());
    }

    @Test
    void getAllCocktailsPaged() {
        Page<Cocktail> page = new PageImpl<>(Arrays.asList(cocktail1, cocktail2));
        Pageable pageable = PageRequest.of(0, 10);

        when(cocktailRepository.findAll(pageable)).thenReturn(page);

        Page<CocktailEntity> result = cocktailService.getAllCocktailsPaged(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Margarita", result.getContent().get(0).getStrDrink());
        assertEquals("Mojito", result.getContent().get(1).getStrDrink());
    }

    @Test
    void getCocktailsByIngredient() {
        when(cocktailRepository.findByStrIngredientsContaining("Tequila")).thenReturn(Arrays.asList(cocktail1));

        List<CocktailEntity> result = cocktailService.getCocktailsByIngredient("Tequila");

        assertEquals(1, result.size());
        assertEquals("Margarita", result.get(0).getStrDrink());
    }

    @Test
    void getCocktailById() {
        when(cocktailRepository.findById("1")).thenReturn(Optional.of(cocktail1));

        Optional<CocktailEntity> result = cocktailService.getCocktailById("1");

        assertTrue(result.isPresent());
        assertEquals("Margarita", result.get().getStrDrink());
    }
}*/
