package tests;

import core.config.ConfigReader;
import core.driver.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import listeners.AllureTestListener;
import org.testng.annotations.Listeners;


@Listeners(AllureTestListener.class)
public abstract class BaseTest {

    @BeforeSuite(alwaysRun = true)
    public void logSuiteEnvironment() {
        System.out.println("=== Initializing Test Suite ===");
        System.out.println("Browser : " + ConfigReader.get("browser"));
        System.out.println("Base URL: " + ConfigReader.get("base.url"));
    }

    @BeforeMethod(alwaysRun = true)
    public void setUpBrowser() {
        DriverManager.initDriver(ConfigReader.get("browser"));
        DriverManager.getDriver().get(ConfigReader.get("base.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDownBrowser() {
        DriverManager.quitDriver();
    }
}
