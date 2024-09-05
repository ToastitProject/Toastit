package alcoholboot.toastit.feature.user.service.impl;

import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.image.service.CloudStorageService;
import alcoholboot.toastit.feature.image.service.ImageService;
import alcoholboot.toastit.feature.user.domain.User;
import alcoholboot.toastit.feature.user.service.UserProfileImageService;
import alcoholboot.toastit.feature.user.service.UserManagementService;
import alcoholboot.toastit.global.config.response.code.CommonExceptionCode;
import alcoholboot.toastit.global.config.response.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 사용자 프로필 이미지 관리와 관련된 비즈니스 로직을 처리하는 서비스 구현 클래스.
 * 클라우드 스토리지에 이미지를 업로드하고, 사용자 정보를 업데이트하는 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
public class UserProfileImageServiceImpl implements UserProfileImageService {

    private final CloudStorageService cloudStorageService;
    private final ImageService imageService;
    private final UserManagementService userManagementService;

    /**
     * 사용자 프로필 이미지를 변경하는 메서드.
     *
     * @param email    프로필 사진을 변경하고자 하는 사용자의 이메일
     * @param filePath 새로 업로드할 프로필 사진 파일
     * @throws Exception 이미지 업로드 및 저장 중 발생할 수 있는 예외
     */
    @Override
    @Transactional
    public void changeUserProfileImage(String email, MultipartFile filePath) throws Exception {
        User user = userManagementService.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER));

        // 클라우드 스토리지에 프로필 이미지 업로드
        String imageUrl = cloudStorageService.uploadProfileImage(filePath);

        // 기존 이미지 엔티티 조회 또는 새로운 이미지 엔티티 생성
        ImageEntity imageEntity = imageService.findByUserId(user.getId());
        if (imageEntity != null) {
            updateImageEntity(imageEntity, filePath, imageUrl);
        } else {
            imageEntity = createNewImageEntity(user, filePath, imageUrl);
        }

        // 이미지 정보 저장
        imageService.save(imageEntity);

        // URL 경로 변환
        String newUrl = imageUrl.replace("https://s3.amazonaws.com/toastitbucket",
                "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");

        // 사용자 프로필 이미지 URL 업데이트
        user.setProfileImageUrl(newUrl);
        userManagementService.save(user.convertToEntity());
    }

    /**
     * 기존 이미지 엔티티를 업데이트하는 메서드.
     *
     * @param imageEntity 업데이트할 이미지 엔티티
     * @param filePath    이미지 파일 경로
     * @param imageUrl    이미지의 URL
     */
    private void updateImageEntity(ImageEntity imageEntity, MultipartFile filePath, String imageUrl) {
        imageEntity.setImageName(filePath.getOriginalFilename());
        imageEntity.setImagePath(imageUrl);
        imageEntity.setImageType(filePath.getContentType());
        imageEntity.setImageSize(String.valueOf(filePath.getSize()));
        imageEntity.setImageUse("profile");
    }

    /**
     * 새로운 이미지 엔티티를 생성하는 메서드.
     *
     * @param user      이미지와 연결된 사용자 객체
     * @param filePath  이미지 파일 경로
     * @param imageUrl  이미지의 URL
     * @return 생성된 이미지 엔티티 객체
     */
    private ImageEntity createNewImageEntity(User user, MultipartFile filePath, String imageUrl) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setId(user.getId());
        imageEntity.setImageName(filePath.getOriginalFilename());
        imageEntity.setImagePath(imageUrl);
        imageEntity.setImageType(filePath.getContentType());
        imageEntity.setImageSize(String.valueOf(filePath.getSize()));
        imageEntity.setImageUse("profile");
        return imageEntity;
    }
}