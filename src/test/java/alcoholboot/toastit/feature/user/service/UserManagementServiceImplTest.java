package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.entity.UserEntity;
import alcoholboot.toastit.feature.user.repository.UserRepository;
import alcoholboot.toastit.feature.user.service.impl.UserManagementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserManagementServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserManagementServiceImpl userService;

    /**
     * @ExtendWith(MockitoExtension.class) 로 해결
     */
//    public UserManagementServiceImplTest() {
//        MockitoAnnotations.openMocks(this);
//    }

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