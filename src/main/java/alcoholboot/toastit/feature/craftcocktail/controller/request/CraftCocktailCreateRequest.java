package alcoholboot.toastit.feature.craftcocktail.controller.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 입력 레시피 요청 객체
 * 칵테일, 재료 테이블 포함
 */
@Getter
@Setter
public class CraftCocktailCreateRequest {

    private String name;
    private String description;
    private String recipe;
    private MultipartFile image;
    private List<IngredientDTO> ingredients = new ArrayList<>();

    @Getter
    @Setter
    public static class IngredientDTO {
        private String name;
        private String amount;
        private String unit;
    }
}