package alcoholboot.toastit.feature.categorysearch.repository;

import alcoholboot.toastit.feature.categorysearch.domain.Cocktail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataMongoTest
class CocktailRepositoryTest {

    @Autowired
    private CocktailRepository cocktailRepository;

    @Test
    void testFindByStrIngredientsContaining() {
        Page<Cocktail> result = cocktailRepository.findByStrIngredientsContaining("Vodka", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Cocktail", result.getContent().get(0).getStrDrink());
    }
    // Add more tests for other repository methods
}