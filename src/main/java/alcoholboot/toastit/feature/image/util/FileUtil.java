package alcoholboot.toastit.feature.image.util;

import lombok.experimental.UtilityClass;

import net.coobird.thumbnailator.Thumbnails;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.UUID;

@UtilityClass
public class FileUtil {

    public InputStream resizeImage(InputStream inputStream, int width, int height) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                .size(width, height)
                .toOutputStream(outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    public String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());
    }
}