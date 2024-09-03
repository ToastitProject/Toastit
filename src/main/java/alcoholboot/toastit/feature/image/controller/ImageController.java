package alcoholboot.toastit.feature.image.controller;

import alcoholboot.toastit.feature.image.entity.ImageEntity;
import alcoholboot.toastit.feature.image.service.CloudStorageService;
import alcoholboot.toastit.feature.image.service.ImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import alcoholboot.toastit.feature.image.entity.ImageEntity;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ImageController {
    private final CloudStorageService cloudStorageService;
    private final ImageService imageService;

    @GetMapping("/image")
    public String upload(Model model) {
        System.out.println("GetMapping 요청 처리:::::::::::::::::::::::::");
        List<ImageEntity> imageEntities = imageService.findAll();
        model.addAttribute("images", imageEntities);
        return "image";
    }

    @PostMapping("/image")
    public String uploadImage(@RequestParam MultipartFile filePath) {
        System.out.println("PostMapping 요청 처리:::::::::::::::::::::::::");
        try {
            String imageUrl = cloudStorageService.uploadStaticImage(filePath);
            System.out.println("AWS 파일 업로드 성공");

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setImageName(filePath.getOriginalFilename());
            imageEntity.setImagePath(imageUrl);
            imageEntity.setImageType(filePath.getContentType());
            imageEntity.setImageSize(String.valueOf(filePath.getSize()));
            imageEntity.setImageUse("static");
            imageService.save(imageEntity);
            System.out.println("MySQL 파일 저장 성공");

            return "redirect:/image";
        } catch (Exception e) {
            System.out.println("파일 업로드 실패" + e.getMessage());
            return "redirect:/image";
        }
    }
}
