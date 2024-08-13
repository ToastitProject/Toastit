package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.repository.UserRepository;
import alcoholboot.toastit.feature.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        // Given
        UserEntity userEntity = new UserEntity();

        userEntity.setNickname("testuser");

        // When
        userService.save(userEntity);

        // Then
        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userEntityCaptor.capture());
        UserEntity savedUserEntity = userEntityCaptor.getValue();
        assertEquals("testuser", savedUserEntity.getNickname());

    }
}
