package core.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER_THREAD_LOCAL = new ThreadLocal<>();

    private DriverManager() {}

    public static void initDriver(String browser) {
        if (DRIVER_THREAD_LOCAL.get() != null) {
            return;
        }

        boolean isHeadless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        WebDriver driver = switch (browser.toLowerCase()) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (isHeadless) {
                    options.addArguments("-headless", "--width=1920", "--height=1080");
                }
                yield new FirefoxDriver(options);
            }
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--no-sandbox", "--disable-dev-shm-usage");
                if (isHeadless) {
                    options.addArguments("--headless=new", "--window-size=1920,1080");
                }
                yield new ChromeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
        DRIVER_THREAD_LOCAL.set(driver);
    }

    public static boolean hasDriver() {
        return DRIVER_THREAD_LOCAL.get() != null;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver is not initialized for current thread.");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER_THREAD_LOCAL.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER_THREAD_LOCAL.remove();
            }
        }
    }
}






















    

