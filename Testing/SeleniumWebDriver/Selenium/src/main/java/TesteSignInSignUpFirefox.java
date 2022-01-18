import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;


public class TesteSignInSignUpFirefox {
    private WebDriver driver;

    @Before
    public void inicializarFirefoxDriver(){
        System.setProperty("webdriver.gecko.driver", "geckodriver");
        driver = new FirefoxDriver();
        driver.get("http://localhost:3000");
    }
    /* @After
        public void quitDriver(){
            driver.quit();
        }*/
    @Test
    public void crearCuentaUsuarioFirefox() {
        driver.findElement(By.className("nav_btnLinkMenu__2CCzd")).click();
        driver.findElement(By.id("firstName")).sendKeys("Pablo");
        driver.findElement(By.id("lastName")).sendKeys("Neruda");
        driver.findElement(By.id("email")).sendKeys("pepe@gmail.com");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("passwordRepeat")).sendKeys("123456");
        driver.findElement(By.id("btnCrearCuentaSign")).click();
        driver.get("http://localhost:3000/registro-exito");
        Assert.assertTrue(driver.findElement(By.className("registroOk_mensaje__1V-mq")).getText().contains("Su " +
                "registro se ha realizado con éxito."));
        // A depender del monitor, no pasa el test, standard es de zoom 100%, cambiar width o scroll no funcionan
        //driver.manage().window().setSize(new Dimension(1440, 1000));
        //JavascriptExecutor jse = (JavascriptExecutor)driver;
        //jse.executeScript("window.scrollBy(100,0)");
        //jse.executeScript("window.scrollTo(100, document.body.scrollHeight)");
    }
    @Test
    public void crearCuentayIniciarSinCuentaHabilitadaFirefox() {
        driver.findElement(By.className("nav_btnLinkMenu__2CCzd")).click();
        driver.findElement(By.id("firstName")).sendKeys("Pablo");
        driver.findElement(By.id("lastName")).sendKeys("Neruda");
        driver.findElement(By.id("email")).sendKeys("pner@gmail.com");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("passwordRepeat")).sendKeys("123456");
        driver.findElement(By.id("btnCrearCuentaSign")).click();
        driver.get("http://localhost:3000/registro-exito");
        driver.findElement(By.id("bookingOk")).click();
        driver.get("http://localhost:3000");
        driver.findElement(By.id("btnIniciar")).click();
        driver.findElement(By.id("email")).sendKeys("pner@gmail.com");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.id("btnIngresar")).click();
        String errorCredentialsMsg= driver.findElement(By.className("form_errorMsg__7aaAG")).getText();
        Assert.assertTrue(errorCredentialsMsg.contains("Su cuenta aún no ha sido habilitada."));
    }
    @Test
    public void pruebaNoIniciarSesionSinCuentaFirefox() {
        driver.findElement(By.id("btnIniciar")).click();
        driver.findElement(By.id("email")).sendKeys("fake@gmail.com");
        driver.findElement(By.id("password")).sendKeys("111111");
        driver.findElement(By.id("btnIngresar")).click();
        String errorCredentialsMsg= driver.findElement(By.className("form_errorMsg__7aaAG")).getText();
        Assert.assertTrue(errorCredentialsMsg.contains("El usuario con el email fake@gmail.com no existe"));
    }

}
