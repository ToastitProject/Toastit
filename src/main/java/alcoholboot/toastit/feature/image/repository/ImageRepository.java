package alcoholboot.toastit.feature.image.repository;

import alcoholboot.toastit.feature.image.entity.ImageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<ImageEntity, Long> {
    ImageEntity findByUserId(Long userId);
}
