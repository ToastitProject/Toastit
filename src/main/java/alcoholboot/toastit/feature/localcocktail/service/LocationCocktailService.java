package alcoholboot.toastit.feature.localcocktail.service;

import alcoholboot.toastit.feature.basecocktail.domain.Cocktail;

import java.util.List;

/**
 * 위치 기반 칵테일 서비스를 위한 인터페이스
 */
public interface LocationCocktailService {

    /**
     * 시와 도에 따른 재료를 기반으로 추천 칵테일을 가져옴
     *
     * @param city 시 정보
     * @param province 도 정보
     * @return 추천된 칵테일 또는 null (재료를 찾을 수 없을 때)
     */
    Cocktail getCocktailForLocation(String city, String province);

    /**
     * 시와 도에 따른 재료 조회
     *
     * @param si 시 정보
     * @param deo 도 정보
     * @return 재료 목록을 포함한 문자열
     */
    String getIngredients(String si, String deo);

    /**
     * 재료 목록에 따라 무작위로 칵테일 추천
     *
     * @param ingredients 재료 목록
     * @return 추천된 칵테일
     */
    Cocktail getRandomCocktailByIngredients(List<String> ingredients);
}
