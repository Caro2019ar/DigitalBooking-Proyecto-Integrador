import org.junit.Before;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


public class TesteBuscadorChrome {
    private WebDriver driver;

    @Before
    public void inicializarChromeDriver(){
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
    }

    @Test
    public void selecionarCiudadFechasYBuscarChrome() {
        driver.findElement(By.id("react-select-3-input")).sendKeys("Argentina" + Keys.ENTER);
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/form/div/div[2]/div[2]/div/input")).click();
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/form/div/div[2]/div[3]/div[2]/div/div/div[3]/div[2]/div[2]/div[4]")).click();
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/form/div/div[2]/div[3]/div[2]/div/div/div[3]/div[2]/div[2]/div[5]")).click();
        driver.findElement(By.xpath("/html/body/div/div/main/div[1]/form/div/div[2]/div[3]/div[2]/div/div/div[5]/button")).click();
        driver.findElement(By.className("buscador_button__4zPjW")).click();
//        Assert.assertFalse(driver.findElement(By.className("buscador_span_error_message__2CYUR")).isDisplayed());
    }
}
