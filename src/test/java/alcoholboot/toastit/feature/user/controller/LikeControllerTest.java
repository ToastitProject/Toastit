package alcoholboot.toastit.feature.user.controller;

import alcoholboot.toastit.feature.categorysearch.entity.CocktailEntity;
import alcoholboot.toastit.feature.categorysearch.service.CocktailService;
import alcoholboot.toastit.feature.user.domain.Like;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.LikeEntity;
import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.service.LikeService;
import alcoholboot.toastit.feature.user.service.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private CocktailService cocktailService;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private LikeController likeController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(likeController).build();
    }

    @Test
    void testLike_AddLike() {
        // Mocking security context and authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String userEmail = "testuser@example.com";
        when(authentication.getName()).thenReturn(userEmail);

        // Setting up mock user, cocktail and like objects
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(1L); // Long 타입으로 설정
        User mockUser = mockUserEntity.convertToDomain(); // UserEntity를 User 도메인 객체로 변환

        CocktailEntity mockCocktailEntity = new CocktailEntity();
        ObjectId cocktailId = new ObjectId(); // ObjectId 생성
        mockCocktailEntity.setId(cocktailId);

        LikeEntity mockLikeEntity = new LikeEntity();
        mockLikeEntity.setUser(mockUserEntity);
        mockLikeEntity.setCocktail(mockCocktailEntity);

        Like mockLike = mockLikeEntity.convertToDomain(); // LikeEntity를 Like 도메인 객체로 변환

        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(cocktailService.getCocktailById(cocktailId.toHexString())).thenReturn(Optional.of(mockCocktailEntity));
        when(likeService.findByUserIdAndCocktailId(mockUser.getId(), cocktailId)).thenReturn(Optional.empty());

        // Perform the test
        String result = likeController.like(cocktailId.toHexString());

        // Verify the behaviors
        verify(likeService, times(1)).save(any(Like.class));
        assertEquals("redirect:/feature/user/mypage", result);
    }

    @Test
    void testLike_RemoveLike() {
        // Mocking security context and authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        String userEmail = "testuser@example.com";
        when(authentication.getName()).thenReturn(userEmail);

        // Setting up mock user, cocktail and like objects
        UserEntity mockUserEntity = new UserEntity();
        mockUserEntity.setId(1L); // Long 타입으로 설정
        User mockUser = mockUserEntity.convertToDomain(); // UserEntity를 User 도메인 객체로 변환

        CocktailEntity mockCocktailEntity = new CocktailEntity();
        ObjectId cocktailId = new ObjectId(); // ObjectId 생성
        mockCocktailEntity.setId(cocktailId);

        LikeEntity mockLikeEntity = new LikeEntity();
        mockLikeEntity.setUser(mockUserEntity);
        mockLikeEntity.setCocktail(mockCocktailEntity);


        when(userService.findByEmail(userEmail)).thenReturn(Optional.of(mockUser));
        when(cocktailService.getCocktailById(cocktailId.toHexString())).thenReturn(Optional.of(mockCocktailEntity));
        when(likeService.findByUserIdAndCocktailId(mockUser.getId(), cocktailId)).thenReturn(Optional.of(mockLikeEntity));

        // Perform the test
        String result = likeController.like(cocktailId.toHexString());

        // Verify the behaviors
        verify(likeService, times(1)).delete(any(Like.class));
        assertEquals("redirect:/feature/user/mypage", result);
    }
}