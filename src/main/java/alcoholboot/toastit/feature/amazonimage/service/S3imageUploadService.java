package alcoholboot.toastit.feature.amazonimage.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3imageUploadService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String deFaultUrl = "https://s3.amazonaws.com/";

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = generateFileName(file);
        return uploadToS3(file, fileName);
    }
    //홈페이지에 필요한 기본 사진들을 업로드 하는 메서드
    public String uploadStaticImage(MultipartFile file) throws IOException {
        String fileName = "static/" + generateFileName(file);
        return uploadToS3(file, fileName);
    }
    //기본 칵테일 사진 업로드 하는 메서드
    public String uploadCocktailImage(MultipartFile file) throws IOException {
        String fileName = "cocktail/" + generateFileName(file);
        return uploadToS3(file, fileName);
    }
    //프로필 사진을 업로드 하는 메서드
    public String uploadProfileImage(MultipartFile file) throws IOException {
        String fileName = "profile/" + generateFileName(file);
        return uploadToS3(file, fileName);
    }
    //커스텀 레시피 칵테일 사진을 업로드 하는 메서드
    public String uploadCustomImage(MultipartFile file) throws IOException {
        String fileName = "custom/" + generateFileName(file);
        return uploadToS3(file, fileName);
    }

    private String uploadToS3(MultipartFile file, String fileName) throws IOException {
        try {
            InputStream resizedImageStream = resizeImage(file.getInputStream(), 300, 300); //300 * 300 으로 일괄 저장

            ObjectMetadata metadata = getObjectMetadata(file);
            metadata.setContentLength(resizedImageStream.available());
            s3Client.putObject(bucketName, fileName, resizedImageStream, metadata);
            return deFaultUrl + bucketName + "/" + fileName; // URL 형식 수정
        } catch (SdkClientException e) {
            throw new IOException(e);
        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "." + getFileExtension(file.getOriginalFilename());
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }
    private InputStream resizeImage(InputStream inputStream, int width, int height) throws IOException {
        // 이미지 리사이징
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                .size(width, height)
                .toOutputStream(outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
