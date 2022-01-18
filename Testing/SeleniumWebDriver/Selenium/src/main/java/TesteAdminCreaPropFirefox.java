import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;


public class TesteAdminCreaPropFirefox {
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
    public void adminLoginYCreaPropiedadFirefox() {
        driver.get("http://localhost:3000/login");
        driver.findElement(By.id("email")).sendKeys("admin@gmail.com");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.id("btnIngresar")).click();
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException ie){
        }
        driver.get("http://localhost:3000/admin/newProduct");
        driver.findElement(By.id("name")).sendKeys("Hotel Panargentino");
        WebElement divCategoria = driver.findElement(By.xpath("/html/body/div/div/main/div[2]/form/div[1]/div/div[2" +
                "]/div/div/div"));
        divCategoria.click();
        WebElement inputField = driver.findElement(By.id("react-select-7-input"));
        inputField.sendKeys("Hotel Urbano"+Keys.ENTER);
        driver.findElement(By.xpath("//*[@id=\"address\"]")).sendKeys("Calle Panargentino");

        WebElement divCiudad = driver.findElement(By.xpath("/html/body/div/div/main/div[2]/form/div[1]/div/div[4]/div/div/div/div[1]/div[2]"));
        divCiudad.click();
        WebElement inputFieldCiudad = driver.findElement(By.id("react-select-9-input"));
        inputFieldCiudad.sendKeys("Mendoza"+Keys.ENTER);
        driver.findElement(By.id("latitude")).sendKeys("333");
        driver.findElement(By.id("longitude")).sendKeys("4444");
        WebElement textareaDescripcion = driver.findElement(By.id("description"));
        textareaDescripcion.sendKeys("Hotel Panargentino en Bariloche con 450 habitaciones.Libérate del estrés con un entrenamiento en el gimnasio cuando te apetezca. O disfruta de las vistas mientras te relajas en la piscina del último piso");

       //scroll hasta Agregar Atributos
        WebElement atributos =  driver.findElement(By.id("description"));
        jse.executeScript("arguments[0].scrollIntoView();", atributos);

        WebElement wiFi = driver.findElement(By.xpath("/html/body/div/div/main/div[2]/form/div[2]/div/div[2]/label" +
                "/span[1]"));
        wiFi.click();

        WebElement normasDeLaCasa = driver.findElement(By.xpath("//*[@id=\"norms\"]"));
        normasDeLaCasa.sendKeys("Se ruega no pisar el césped");
        WebElement salud = driver.findElement(By.xpath("//*[@id=\"security\"]"));
        salud.sendKeys("Se ruega mantener la prolijidad y limpieza de la casa");
        WebElement cancelacion = driver.findElement(By.xpath("//*[@id=\"politics\"]"));
        cancelacion.sendKeys("Cancelamiento con 24hs de antelación");

        //scroll hasta Cargar imagenes
        WebElement imagenes =  driver.findElement(By.xpath("/html/body/div/div/main/div[2]/form/div[4]/h3"));
        jse.executeScript("arguments[0].scrollIntoView();", imagenes);

        driver.findElement(By.id("nuevaImagen")).sendKeys("https://www.fotos123.com");
        WebElement botonMas = driver.findElement(By.xpath("//*[@id=\"btnImages\"]"));
        botonMas.click();

        //scroll hasta Crear

        WebElement botonCrear = driver.findElement(By.xpath("//*[@id=\"btnCrear\"]"));
        jse.executeScript("arguments[0].scrollIntoView();", botonCrear);

        botonCrear.click();
        Assert.assertFalse(driver.findElement(By.className("form_errorBookingMsg__1YwxY")).getText().contains("Por favor, verifica que todos los campos fueron completados\n"));
    }


}
