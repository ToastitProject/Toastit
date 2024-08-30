package alcoholboot.toastit.feature.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudStorageService {

    String uploadImage(MultipartFile file) throws IOException;

    String uploadStaticImage(MultipartFile file) throws IOException;

    String uploadCocktailImage(MultipartFile file) throws IOException;

    String uploadProfileImage(MultipartFile file) throws IOException;

    String uploadCraftCocktailImage(MultipartFile file) throws IOException;
}