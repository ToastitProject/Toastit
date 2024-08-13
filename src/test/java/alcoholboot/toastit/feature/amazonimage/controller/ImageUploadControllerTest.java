package alcoholboot.toastit.feature.amazonimage.controller;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import alcoholboot.toastit.feature.amazonimage.domain.Image;
import alcoholboot.toastit.feature.amazonimage.service.ImageService;
import alcoholboot.toastit.feature.amazonimage.service.S3imageUploadService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
public class ImageUploadControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private S3imageUploadService s3imageUploadService;
//
//    @Mock
//    private ImageService imageService;
//
//    @InjectMocks
//    private ImageUploadController imageUploadController;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(imageUploadController).build();
//    }
//
//    @Test
//    public void testUploadPage() throws Exception {
//
//        List<Image> images = new ArrayList<>();
//        when(imageService.findAll()).thenReturn(images);
//
//        mockMvc.perform(get("/image"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("image"))
//                .andExpect(model().attributeExists("images"))
//                .andDo(print());
//    }
//
//    @Test
//    public void testUploadImage() throws Exception {
//
//        MultipartFile mockFile = mock(MultipartFile.class);
//        when(mockFile.getOriginalFilename()).thenReturn("test.jpg");
//        when(mockFile.getContentType()).thenReturn("image/jpeg");
//        when(mockFile.getSize()).thenReturn(1024L);
//
//        String imageUrl = "https://toastitbucket.s3.ap-northeast-2.amazonaws.com/test.jpg";
//        when(s3imageUploadService.uploadImage(mockFile)).thenReturn(imageUrl);
//
//
//        mockMvc.perform(multipart("/image")
//                        .file("filePath", mockFile.getBytes())
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/image"))
//                .andDo(print());
//
//
//        verify(imageService).save(any(Image.class));
//    }
}