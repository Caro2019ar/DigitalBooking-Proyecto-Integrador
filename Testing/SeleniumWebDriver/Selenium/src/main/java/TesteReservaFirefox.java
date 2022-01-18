import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;


public class TesteReservaFirefox {
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
    public void reservaDeUsuarioFirefox() {
       driver.findElement(By.id("btnIniciar")).click();
        driver.findElement(By.id("email")).sendKeys("juntam2015@gmail.com");
        driver.findElement(By.id("password")).sendKeys("111111");
        driver.findElement(By.id("btnIngresar")).click();
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ie){
        }
        WebElement bajaHastaRecomendaciones =  driver.findElement(By.xpath(
                "/html/body/div/div/main/div[2]/div/div[1]/p"));
        jse.executeScript("arguments[0].scrollIntoView();", bajaHastaRecomendaciones);
        driver.findElement(By.className("global_button__ohhFS")).click();
        WebElement searchBox =  driver.findElement(By.xpath("/html/body/div/div/main/div[5]/div[1]/h2"));
        jse.executeScript("arguments[0].scrollIntoView();", searchBox);
        driver.findElement(By.className("global_button__ohhFS")).click();
        driver.findElement(By.id("city")).click();
        driver.findElement(By.id("city")).sendKeys("Pilar"+ Keys.ENTER);
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ie){
        }
        WebElement confirmarReserva =  driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div[4]/div[2]/div" +
                "[5]/button"));
        jse.executeScript("arguments[0].scrollIntoView();", confirmarReserva);
       driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div[2]/div/div/div/div[3]/div[2]/div[4]/div[1]")).click();
       driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div[2]/div/div/div/div[3]/div[2]/div[4]/div[2]")).click();
       driver.findElement(By.xpath("//*[@id=\"Entrytime\"]")).click();
       driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div[3]/div/div/select/option[11]")).click();
        WebElement confirmarReserva2 =  driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div[4]/div[2]/div[1]/div/div/h4"));
        jse.executeScript("arguments[0].scrollIntoView();", confirmarReserva2);
        driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div[4]/div[2]/div" +
                "[5]/button")).click();
        driver.findElement(By.xpath("/html/body/div/div/main/div[2]/div[5]/div/div[2]/div[2]")).click();
        Assert.assertTrue(driver.findElement(By.className("bookingOkStyle_h5__2Ovgj")).getText().contains("Su reserva se ha realizado con éxito"));
    }


}
