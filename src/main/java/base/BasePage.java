package base;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BasePage {

    // Thread local for enabling parallel execution
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static Properties prop = new Properties();
    private static final Logger logger = LogManager.getLogger(BasePage.class);

    static {
        try {
            FileInputStream data = new FileInputStream(
                Paths.get(System.getProperty("user.dir"), "src", "main", "java", "resources", "config.properties").toString()
            );
            prop.load(data);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load config.properties");
        }
    }

    @BeforeTest
    @Parameters({"os", "browser"})
    public void setup(@Optional("Linux") String os, @Optional("chrome") String br) throws IOException {

        logger.info("Running on OS: " + os + ", Browser: " + br);
        //Desiredcapabilities to select the os and browser
        if (prop.getProperty("execution_env").equalsIgnoreCase("remote")) {
            logger.info("Setting up Remote WebDriver");
            DesiredCapabilities capabilities = new DesiredCapabilities();

            logger.info("Selecting Operating System");
            if (os.equalsIgnoreCase("Windows")) {
                capabilities.setPlatform(Platform.WIN11);
            } else if (os.equalsIgnoreCase("Linux")) {
                capabilities.setPlatform(Platform.LINUX);
            } else {
                logger.error("No matching OS for platform");
                return;
            }

            logger.info("Selecting browser");
            switch (br.toLowerCase()) {
                case "chrome": capabilities.setBrowserName("chrome"); break;
                case "edge": capabilities.setBrowserName("MicrosoftEdge"); break;
                case "firefox": capabilities.setBrowserName("firefox"); break;
                default: throw new IllegalArgumentException("Unsupported browser: " + br);
            }
            //passing to remote webdriver also assign a driver instance to test thread
            driver.set(new RemoteWebDriver(URI.create("http://localhost:4444/wd/hub").toURL(), capabilities));
            //if local is enabled on config
        } else if (prop.getProperty("execution_env").equalsIgnoreCase("local")) {
            logger.info("Setting up Local WebDriver");
            switch (br.toLowerCase()) {
                case "chrome": driver.set(new ChromeDriver()); break;
                case "edge": driver.set(new EdgeDriver()); break;
                case "firefox": driver.set(new FirefoxDriver()); break;
                default: throw new IllegalArgumentException("Unsupported browser: " + br);
            }
        }

        WebDriver webDriver = getDriver();
        webDriver.manage().window().maximize();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.get(getUrl());
        logger.info("Navigated to URL: " + getUrl());
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        WebDriver webDriver = driver.get();
        if (webDriver != null) {
            webDriver.quit();
            driver.remove(); 
        }
    }

    public static WebDriver getDriver() {
    	//fetch the driver for current thread
        WebDriver webDriver = driver.get();
        if (webDriver == null) {
            throw new IllegalStateException("WebDriver not initialized");
        }
        return webDriver;
    }

    public static String getUrl() {
        String url = prop.getProperty("url");
        if (url == null || url.isEmpty()) {
            throw new RuntimeException("URL is not specified in config.properties");
        }
        return url;
    }
}
