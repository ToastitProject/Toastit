package alcoholboot.toastit.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorPageTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\ProgramData\\Microsoft\\Windows\\Start Menu\\Programs");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testErrorPage() {
        driver.get("http://localhost:8080/non-existent");

        // Allow some time for the error page to load and JavaScript to fetch the error details
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement errorCodeElement = driver.findElement(By.id("error-code"));
        WebElement messageElement = driver.findElement(By.id("message"));

        assertEquals("404", errorCodeElement.getText());
        assertEquals("존재하지 않는 API입니다.", messageElement.getText());
    }
}

