package tests;

import core.driver.DriverManager;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class SmokeTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverManager.initDriver("chrome");
    }

    @Test
    public void verifyBrowserLaunch() {
        DriverManager.getDriver().get("https://www.selenium.dev");
        String title = DriverManager.getDriver().getTitle();
        System.out.println("Page title: " + title);
        Assert.assertTrue(title.contains("Selenium"), "Unexpected page title!");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }
}


