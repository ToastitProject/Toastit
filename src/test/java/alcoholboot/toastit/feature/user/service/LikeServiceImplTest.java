package alcoholboot.toastit.feature.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import alcoholboot.toastit.feature.user.domain.Like;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.repository.LikeRepository;
import alcoholboot.toastit.feature.user.service.impl.LikeServiceImpl;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LikeServiceImplTest {
    //필요한 객체들 생성
    @Mock
    private LikeRepository likeRepository;

    @InjectMocks
    private LikeServiceImpl likeService;

    private Like like;
    private LikeEntity likeEntity;

    @BeforeEach
    void setUp() {

        like = mock(Like.class);
        likeEntity = new LikeEntity();
        likeEntity.setUser(new User().convertToEntity()); // User 객체 설정
        likeEntity.setCocktail(new CocktailEntity()); // CocktailEntity 객체 설정
    }

    @Test
    void save_ShouldSaveLike() {
        when(like.convertToEntity()).thenReturn(likeEntity);
        likeService.save(like);
        verify(likeRepository, times(1)).save(likeEntity);
        //좋아요 저장 확인
    }

    @Test
    void delete_ShouldDeleteLike() {
        when(like.convertToEntity()).thenReturn(likeEntity);
        likeService.delete(like);
        verify(likeRepository, times(1)).delete(likeEntity);
        //좋아요 삭제 확인
    }

    @Test
    void findByUserIdAndCocktailId_ShouldReturnOptionalLikeEntity() {
        Long userId = 1L;
        ObjectId cocktailId = new ObjectId("507f1f77bcf86cd799439011");
        //user id 와 cocktail id 주입

        when(likeRepository.findByUserIdAndCocktailId(userId, cocktailId)).thenReturn(Optional.of(likeEntity));
        Optional<LikeEntity> result = likeService.findByUserIdAndCocktailId(userId, cocktailId);
        assertTrue(result.isPresent());
        assertEquals(likeEntity, result.get());
        verify(likeRepository, times(1)).findByUserIdAndCocktailId(userId, cocktailId);
        //user id 와 cocktail id 가 존재하면 LikeEntity 반환 확인
    }

    @Test
    void findByUserIdAndCocktailId_ShouldReturnEmptyOptional() {
        Long userId = 1L;
        ObjectId cocktailId = new ObjectId("507f1f77bcf86cd799439011");

        when(likeRepository.findByUserIdAndCocktailId(userId, cocktailId)).thenReturn(Optional.empty());
        Optional<LikeEntity> result = likeService.findByUserIdAndCocktailId(userId, cocktailId);
        assertFalse(result.isPresent());
        verify(likeRepository, times(1)).findByUserIdAndCocktailId(userId, cocktailId);
        //user id 와 cocktail id 가 존재하지 않으면 LikeEntity 를 반환하지 않음 확인.

    }
  
}