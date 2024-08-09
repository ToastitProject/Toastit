package alcoholboot.toastit.feature.categorysearch.service.impl;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import alcoholboot.toastit.feature.categorysearch.repository.CocktailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CocktailServiceImplTest {

    @Mock
    private CocktailRepository cocktailRepository;

    @InjectMocks
    private CocktailServiceImpl cocktailService;

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
        PageRequest pageRequest = PageRequest.of(0, 10);

        when(cocktailRepository.findAll(pageRequest)).thenReturn(cocktailPage);

        Page<CocktailEntity> result = cocktailService.getAllCocktails(pageRequest);

        assertEquals(cocktailPage, result);
    }

    @Test
    void testGetCocktailById() {
        CocktailEntity cocktail = new CocktailEntity();
        when(cocktailRepository.findById("1")).thenReturn(Optional.of(cocktail));

        CocktailEntity result = cocktailService.getCocktailById("1");

        assertEquals(cocktail, result);
    }

    @Test
    void testGetCocktailByIdNotFound() {
        when(cocktailRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cocktailService.getCocktailById("1"));
    }
}