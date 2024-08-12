package alcoholboot.toastit.feature.user.service;

import alcoholboot.toastit.feature.user.controller.request.UserJoinRequest;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.entity.UserEntity;

import java.util.Optional;

public interface UserService {
    void save(UserJoinRequest userJoinDto);
    String getUniqueNickname();
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByNickname(String nickname);
    void save(UserEntity user);
}