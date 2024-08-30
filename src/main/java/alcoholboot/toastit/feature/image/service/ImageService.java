package alcoholboot.toastit.feature.image.service;

import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageEntity save(ImageEntity imageEntity) {
        return imageRepository.save(imageEntity);
    }

    public List<ImageEntity> findAll() {
        return (List<ImageEntity>) imageRepository.findAll();
    }

    public ImageEntity findByUserId(Long id) {return imageRepository.findByUserId(id);};
}
