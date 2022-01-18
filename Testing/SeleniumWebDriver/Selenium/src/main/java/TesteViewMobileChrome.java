import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TesteViewMobileChrome {

    @Test
    public void definirViewMobileChrome() {
    System.setProperty("webdriver.chrome.driver", "chromedriver");
    WebDriver driver = new ChromeDriver();
    driver.get("http://localhost:3000");
    driver.manage().window().setSize(new Dimension(414,765));
    Assert.assertTrue(driver.findElement(By.id("hamburguesaIcon")).isDisplayed());
}
}
