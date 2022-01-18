import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class TesteSignInSignUpChrome {

    @Test
    public void crearCuentaUsuarioChrome() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        options.addArguments("--headless");
        driver.manage().window().setSize(new Dimension(1440, 900));
        driver.get("http://localhost:3000");
        driver.findElement(By.className("nav_btnLinkMenu__2CCzd")).click();
        driver.findElement(By.id("firstName")).sendKeys("Pablo");
        driver.findElement(By.id("lastName")).sendKeys("Neruda");
        driver.findElement(By.id("email")).sendKeys("pn@gmail.com");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("passwordRepeat")).sendKeys("123456");
        driver.findElement(By.id("btnCrearCuentaSign")).click();
        driver.get("http://localhost:3000/registro-exito");
        Assert.assertTrue(driver.findElement(By.className("registroOk_mensaje__1V-mq")).getText().contains("Su " +
                "registro se ha realizado con éxito."));
    }
    @Test
    public void crearCuentayIniciarSinCuentaHabilitadaChrome() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);
        options.addArguments("--headless");
        driver.manage().window().setSize(new Dimension(1440, 900));
        driver.get("http://localhost:3000");
        driver.findElement(By.className("nav_btnLinkMenu__2CCzd")).click();
        driver.findElement(By.id("firstName")).sendKeys("Pablo");
        driver.findElement(By.id("lastName")).sendKeys("Neruda");
        driver.findElement(By.id("email")).sendKeys("pneruda2@gmail.com");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("passwordRepeat")).sendKeys("123456");
        driver.findElement(By.id("btnCrearCuentaSign")).click();
        driver.get("http://localhost:3000/registro-exito");
        driver.findElement(By.id("bookingOk")).click();
        driver.get("http://localhost:3000");
        driver.findElement(By.id("btnIniciar")).click();
        driver.findElement(By.id("email")).sendKeys("pneruda2@gmail.com");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("btnIngresar")).click();
        String errorCredentialsMsg= driver.findElement(By.className("form_errorMsg__1MGJE")).getText();
        Assert.assertTrue(errorCredentialsMsg.contains("Su cuenta aún no ha sido habilitada."));
    }
    @Test
    public void pruebaNoIniciarSesionSinCuentaChrome() {
        System.setProperty("webdriver.chrome.driver", "chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:3000");
        driver.findElement(By.id("btnIniciar")).click();
        driver.findElement(By.id("email")).sendKeys("fake@gmail.com");
        driver.findElement(By.id("password")).sendKeys("111111");
        driver.findElement(By.id("btnIngresar")).click();
        String errorCredentialsMsg= driver.findElement(By.className("form_errorMsg__1MGJE")).getText();
        Assert.assertTrue(errorCredentialsMsg.contains("El usuario con el email fake@gmail.com no existe"));
    }

}
