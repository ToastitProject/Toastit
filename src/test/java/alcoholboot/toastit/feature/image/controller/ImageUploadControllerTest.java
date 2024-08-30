package alcoholboot.toastit.feature.image.controller;

//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
public class ImageUploadControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private CloudStorageServiceImpl s3imageUploadService;
//
//    @Mock
//    private ImageService imageService;
//
//    @InjectMocks
//    private ImageController imageUploadController;
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