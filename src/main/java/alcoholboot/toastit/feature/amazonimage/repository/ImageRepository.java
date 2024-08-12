package alcoholboot.toastit.feature.amazonimage.repository;

import alcoholboot.toastit.feature.amazonimage.domain.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    Image findByUserId(Long userId);
}
