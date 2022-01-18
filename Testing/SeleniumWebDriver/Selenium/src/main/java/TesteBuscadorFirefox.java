import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TesteBuscadorFirefox {
private WebDriver driver;
    JavascriptExecutor jse;
    @Before
    public void inicializarFirefoxDriver(){
        System.setProperty("webdriver.gecko.driver", "geckodriver");
        driver = new FirefoxDriver();
        driver.get("http://localhost:3000");
        jse = (JavascriptExecutor)driver;
    }

    @Test
    public void selecionarCiudadFechasYBuscarFirefox() {
        driver.findElement(By.id("react-select-3-input")).sendKeys("Argentina" + Keys.ENTER);
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/form/div/div[2]/div[2]/div/input")).click();
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/form/div/div[2]/div[3]/div[2]/div/div/div[3]/div[2]/div[2]/div[4]")).click();
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/form/div/div[2]/div[3]/div[2]/div/div/div[3]/div[2]/div[2]/div[5]")).click();
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/form/div/div[2]/div[3]/div[2]/div/div/div[5]/button")).click();
        driver.findElement(By.className("buscador_button__4zPjW")).click();
        WebElement bajaHastaRecomendaciones =  driver.findElement(By.xpath(
                "/html/body/div/div/main/div[2]/div/div[1]/p"));
        jse.executeScript("arguments[0].scrollIntoView();", bajaHastaRecomendaciones);
        Assert.assertTrue(driver.findElement(By.xpath("/html/body/div/div/main/div[3]/div[1]/div[1]/div[2]/div[2]/div[1" +
                "]/p[1]")).getText().contains("Mendoza"));
    }

}
