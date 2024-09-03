package alcoholboot.toastit.feature.image.service.impl;

import alcoholboot.toastit.feature.image.service.CloudStorageService;
import alcoholboot.toastit.feature.image.util.FileUtil;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class CloudStorageServiceImpl implements CloudStorageService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String defaultUrl = "https://s3.amazonaws.com/";

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    @Override
    public String uploadStaticImage(MultipartFile file) throws IOException {
        String fileName = "static/" + FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    @Override
    public String uploadCocktailImage(MultipartFile file) throws IOException {
        String fileName = "cocktail/" + FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    @Override
    public String uploadProfileImage(MultipartFile file) throws IOException {
        String fileName = "profile/" + FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    @Override
    public String uploadCraftCocktailImage(MultipartFile file) throws IOException {
        String fileName = "custom/" + FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    private String uploadToS3(MultipartFile file, String fileName) throws IOException {
        try {
            InputStream resizedImageStream = FileUtil.resizeImage(file.getInputStream(), 300, 300);

            ObjectMetadata metadata = getObjectMetadata(file, resizedImageStream.available());
            s3Client.putObject(bucketName, fileName, resizedImageStream, metadata);
            return defaultUrl + bucketName + "/" + fileName;
        } catch (SdkClientException e) {
            throw new IOException(e);
        }
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file, long contentLength) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(contentLength);
        return objectMetadata;
    }
}