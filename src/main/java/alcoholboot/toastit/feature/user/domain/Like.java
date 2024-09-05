package alcoholboot.toastit.feature.user.domain;

import alcoholboot.toastit.feature.craftcocktail.entity.CraftCocktailEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;

import lombok.*;

import org.bson.types.ObjectId;

/**
 * 좋아요 정보를 담는 도메인 클래스.
 * 기본 칵테일 또는 커스텀 칵테일에 대한 좋아요 정보를 관리합니다.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Like {

    private Long id;                           // 좋아요 ID
    private Long cocktailId;                   // 커스텀 칵테일 ID
    private ObjectId basecocktailId;           // 기본 칵테일 ID (MongoDB ObjectId)
    private CraftCocktailEntity craftCocktail; // 좋아요한 커스텀 칵테일
    private UserEntity userEntity;             // 좋아요한 사용자 정보
}