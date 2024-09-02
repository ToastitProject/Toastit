package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.UserEntity;

import java.util.Optional;

public interface UserManagementService {
    void save(UserJoinRequest userJoinDto);
    Optional<User> findByEmailAndProviderType(String email, String providerType);
    String getUniqueNickname();
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByNickname(String nickname);
    void save(UserEntity user);
    void deleteByEmail(String email);
    String encryptPassword(String password);
}