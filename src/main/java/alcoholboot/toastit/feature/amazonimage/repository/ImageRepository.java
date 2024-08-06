package alcoholboot.toastit.feature.amazonimage.repository;

import alcoholboot.toastit.global.domain.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
}
