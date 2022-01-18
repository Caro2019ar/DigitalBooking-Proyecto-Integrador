import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TesteViewMobileFirefox {

    @Test
    public void definirViewMobileFirefox() {
        System.setProperty("webdriver.gecko.driver", "geckodriver");
        WebDriver driver = new FirefoxDriver();
        driver.get("http://localhost:3000");
        driver.manage().window().setSize(new Dimension(414,765));
        Assert.assertTrue(driver.findElement(By.id("hamburguesaIcon")).isDisplayed());
    }

}
