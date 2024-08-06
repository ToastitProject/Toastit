package alcoholboot.toastit.feature.amazonimage.controller;

import alcoholboot.toastit.global.domain.Image;
import alcoholboot.toastit.global.service.ImageService;
import alcoholboot.toastit.global.service.S3imageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImageUploadController {
    private final S3imageUploadService s3imageUploadService;
    private final ImageService imageService;

    @GetMapping("/image")
    public String upload(Model model) {
        System.out.println("GetMapping 요청 처리:::::::::::::::::::::::::");
        List<Image> images = imageService.findAll();
        model.addAttribute("images", images);
        return "image";
    }

    @PostMapping("/image")
    public String uploadImage(@RequestParam MultipartFile filePath) {
        System.out.println("PostMapping 요청 처리:::::::::::::::::::::::::");
        try{
            String imageUrl = s3imageUploadService.uploadImage(filePath);
            System.out.println("AWS 파일 업로드 성공");

            Image image = new Image();
            image.setImageName(filePath.getOriginalFilename());
            image.setImagePath(imageUrl);
            image.setImageType(filePath.getContentType());
            image.setImageSize(String.valueOf(filePath.getSize()));
            imageService.save(image);
            System.out.println("MySQL 파일 저장 성공");

            return "redirect:/image" ;
        }catch(Exception e){
            System.out.println("파일 업로드 실패"+e.getMessage());
            return "redirect:/image" ;
        }

    }
}
