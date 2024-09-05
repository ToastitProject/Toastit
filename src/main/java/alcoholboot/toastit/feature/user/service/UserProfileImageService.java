package alcoholboot.toastit.feature.user.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 사용자 프로필 이미지 변경을 위한 서비스 인터페이스.
 * 사용자의 프로필 이미지를 업데이트하는 기능을 제공합니다.
 */
public interface UserProfileImageService {

    /**
     * 사용자의 프로필 이미지를 변경하는 메서드.
     *
     * @param email    프로필 이미지를 변경할 사용자의 이메일
     * @param filePath 변경할 이미지 파일
     * @throws Exception 이미지 업로드 또는 처리 중 발생할 수 있는 예외
     */
    void changeUserProfileImage(String email, MultipartFile filePath) throws Exception;
}