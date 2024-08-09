package alcoholboot.toastit.feature.categorysearch.controller;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CocktailControllerTest {

    @Mock
    private CocktailService cocktailService;

    @Mock
    private Model model;

    @InjectMocks
    private CocktailController cocktailController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCocktails() {
        List<CocktailEntity> cocktails = Arrays.asList(
                new CocktailEntity(), new CocktailEntity()
        );
        Page<CocktailEntity> cocktailPage = new PageImpl<>(cocktails);

        when(cocktailService.getAllCocktails(any(PageRequest.class))).thenReturn(cocktailPage);

        String viewName = cocktailController.getAllCocktails(null, null, null, 0, 10, model);

        assertEquals("cocktailList", viewName);
        verify(model).addAttribute(eq("cocktails"), eq(cocktails));
        verify(model).addAttribute(eq("currentPage"), eq(0));
        verify(model).addAttribute(eq("totalPages"), eq(1));
    }

    @Test
    void testGetCocktailDetails() {
        CocktailEntity cocktail = new CocktailEntity();
        when(cocktailService.getCocktailById("1")).thenReturn(cocktail);

        String viewName = cocktailController.getCocktailDetails("1", model);

        assertEquals("cocktailDetails", viewName);
        verify(model).addAttribute(eq("cocktail"), eq(cocktail));
    }
}