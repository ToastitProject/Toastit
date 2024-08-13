package alcoholboot.toastit.global.config.response.exception;

//@SpringBootTest
//@ActiveProfiles("test")
public class ErrorPageTest {
//
//    private WebDriver driver;
//
//    @BeforeEach
//    public void setUp() {
//        // 윈도우 PC 크롬 브라우저를 기준으로 에러 페이지 테스트 진행
//        System.setProperty("webdriver.chrome.driver", "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs");
//        driver = new ChromeDriver();
//    }
//
//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//
//    @Test
//    public void testErrorPage() {
//        driver.get("http://localhost:8080/non-existent");
//
//        // 오류 페이지가 로드되고 JavaScript가 오류 세부 정보를 가져올 때까지 약간의 시간을 허용한다.
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        WebElement errorCodeElement = driver.findElement(By.id("error-code"));
//        WebElement messageElement = driver.findElement(By.id("message"));
//
//        assertEquals("404", errorCodeElement.getText());
//        assertEquals("존재하지 않는 API입니다.", messageElement.getText());
//    }
}