package alcoholboot.toastit.feature.user.service;

import org.springframework.web.multipart.MultipartFile;

public interface UserProfileImageService {
    void changeUserProfileImage(String email, MultipartFile filePath) throws Exception;
}