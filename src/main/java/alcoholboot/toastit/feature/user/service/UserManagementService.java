package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.auth.controller.request.AuthJoinRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.UserEntity;

import java.util.Optional;

public interface UserManagementService {

    void save(AuthJoinRequest userJoinDto);

    Optional<User> findByEmailAndProviderType(String email, String providerType);

    String getUniqueNickname();

    void updatePassword(String email, String newPassword);

    boolean existsByEmail(String email);

    boolean isSocialLoginEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByNickname(String nickname);

    void save(UserEntity user);

    void deleteByEmail(String email);

    String encryptPassword(String password);
}