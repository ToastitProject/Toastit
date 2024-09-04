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

    /**
     * 일반 사진을 업로드 하면 최상위 폴더에 저장되는 메서드 입니다
     * @param file : 업로드 하고자 하는 파일입니다
     * @return : 파일을 해당 폴더에 업로드 합니다.
     * @throws IOException
     */
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    /**
     * 프로젝트에 쓰일 기본 사진을 업로드 하면 해당 폴더에 저장되는 메서드 입니다
     * @param file : 업로드 하고자 하는 파일입니다
     * @return : 파일을 해당 폴더에 업로드 합니다.
     * @throws IOException
     */
    @Override
    public String uploadStaticImage(MultipartFile file) throws IOException {
        String fileName = "static/" + FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    /**
     * 기본 칵테일 사진을 업로드 하면 해당 폴더에 저장되는 메서드 입니다
     * @param file : 업로드 하고자 하는 파일입니다
     * @return : 파일을 해당 폴더에 업로드 합니다.
     * @throws IOException
     */
    @Override
    public String uploadCocktailImage(MultipartFile file) throws IOException {
        String fileName = "cocktail/" + FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    /**
     * 프로필 사진을 업로드 하면 해당 폴더에 저장되는 메서드 입니다
     * @param file : 업로드 하고자 하는 파일입니다
     * @return : 파일을 해당 폴더에 업로드 합니다.
     * @throws IOException
     */
    @Override
    public String uploadProfileImage(MultipartFile file) throws IOException {
        String fileName = "profile/" + FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    /**
     * 커스템 레시피 사진을 업로드 하면 해당 폴더에 저장되는 메서드 입니다
     * @param file : 업로드 하고자 하는 파일입니다
     * @return : 파일을 해당 폴더에 업로드 합니다.
     * @throws IOException
     */
    @Override
    public String uploadCraftCocktailImage(MultipartFile file) throws IOException {
        String fileName = "custom/" + FileUtil.generateFileName(file);
        return uploadToS3(file, fileName);
    }

    /**
     * Amazon S3 bucket 에 이미지를 업로드 하는 기능입니다
     * @param file : 업로드 하고자 하는 파일입니다.
     * @param fileName : 업로드 하고자 하는 파일 이름입니다.
     * @return : 파일의 이름고 버킷명을 결합하여 리턴 합니다.
     * @throws IOException
     */
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

    /**
     * MultipartFile 객체와 파일의 길이를 입력받아, 업로드할 파일의 메타데이터를 생성합니다.
     * @param file : 업로드 할 파일입니다
     * @param contentLength : 파일의 길이 입니다
     * @return : 메타데이터를 반환합니다
     */
    private ObjectMetadata getObjectMetadata(MultipartFile file, long contentLength) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(contentLength);
        return objectMetadata;
    }
}