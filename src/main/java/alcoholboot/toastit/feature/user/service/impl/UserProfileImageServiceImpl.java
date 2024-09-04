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

@Service
@RequiredArgsConstructor
public class UserProfileImageServiceImpl implements UserProfileImageService {
    private final CloudStorageService cloudStorageService;
    private final ImageService imageService;
    private final UserManagementService userManagementService;

    /**
     * 프로필 이미지를 변경할 때 사용하는 메서드 입니다.
     * @param email : 프로필 사진을 변경하고자 하는 유저를, 등록한 이메일로 찾아옵니다
     * @param filePath : 변경하고자 하는 프로필 사진입니다.
     * @throws Exception
     */
    @Override
    @Transactional
    public void changeUserProfileImage(String email, MultipartFile filePath) throws Exception {
        User user = userManagementService.findByEmail(email)
                .orElseThrow(() -> new CustomException(CommonExceptionCode.NOT_FOUND_USER));

        String imageUrl = cloudStorageService.uploadProfileImage(filePath);

        ImageEntity imageEntity = imageService.findByUserId(user.getId());
        if (imageEntity != null) {
            updateImageEntity(imageEntity, filePath, imageUrl);
        } else {
            imageEntity = createNewImageEntity(user, filePath, imageUrl);
        }
        imageService.save(imageEntity);

        String newUrl = imageUrl.replace("https://s3.amazonaws.com/toastitbucket",
                "https://toastitbucket.s3.ap-northeast-2.amazonaws.com");

        user.setProfileImageUrl(newUrl);
        userManagementService.save(user.convertToEntity());
    }

    /**
     * 이미지 엔티티를 업데이트 하는 메서드 입니다.
     * @param imageEntity : 업로드 하고자 하는 이미지 입니다.
     * @param filePath : 이미지의 경로입니다.
     * @param imageUrl : 이미지의 URL 입니다.
     */
    private void updateImageEntity(ImageEntity imageEntity, MultipartFile filePath, String imageUrl) {
        imageEntity.setImageName(filePath.getOriginalFilename());
        imageEntity.setImagePath(imageUrl);
        imageEntity.setImageType(filePath.getContentType());
        imageEntity.setImageSize(String.valueOf(filePath.getSize()));
        imageEntity.setImageUse("profile");
    }

    /**
     * 새로운 이미지 객체를 만드는 메서드 입니다.
     * @param user : 새로운 이미지 객체를 갖는 사용자 객체 입니다.
     * @param filePath : 새로 업로드 하고자 하는 이미지의 경로입니다.
     * @param imageUrl : 새로 업로드 하고자 하는 이미지의 URL 입니다.
     * @return
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