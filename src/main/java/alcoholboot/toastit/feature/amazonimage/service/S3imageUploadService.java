package alcoholboot.toastit.feature.amazonimage.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3imageUploadService {

    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String deFaultUrl = "https://s3.amazonaws.com";

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = generateFileName(file);
        try{
            s3Client.putObject(bucketName,fileName,file.getInputStream(),getObjectMetadata(file));
            return deFaultUrl+fileName;
        }catch(SdkClientException e){
            throw new IOException(e);
        }
    }

    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "." + file.getOriginalFilename();
    }

    private ObjectMetadata getObjectMetadata(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }
}
