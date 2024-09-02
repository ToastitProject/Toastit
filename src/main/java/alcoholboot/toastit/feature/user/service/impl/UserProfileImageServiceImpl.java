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

    private void updateImageEntity(ImageEntity imageEntity, MultipartFile filePath, String imageUrl) {
        imageEntity.setImageName(filePath.getOriginalFilename());
        imageEntity.setImagePath(imageUrl);
        imageEntity.setImageType(filePath.getContentType());
        imageEntity.setImageSize(String.valueOf(filePath.getSize()));
        imageEntity.setImageUse("profile");
    }

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