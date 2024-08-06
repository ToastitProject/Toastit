package controller;

import service.S3imageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageUploadController {
    private final S3imageUploadService s3imageUploadService;

    @GetMapping("/image")
    public String upload() {
        return "image";
    }

    @PostMapping("/image")
    public String uploadImage(@RequestParam MultipartFile filePath) {
        try{
            s3imageUploadService.uploadImage(filePath);
            System.out.println("파일 업로드 성공");
            return "redirect:/image" ;
        }catch(Exception e){
            System.out.println("파일 업로드 실패"+e.getMessage());
            return "redirect:/image" ;
        }

    }
}
