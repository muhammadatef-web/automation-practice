package listeners;

import core.driver.DriverManager;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;

public class AllureTestListener implements IHookable {

    @Override
    public void run(IHookCallBack callBack, ITestResult testResult) {
        callBack.runTestMethod(testResult);

        if (testResult.getThrowable() != null && DriverManager.hasDriver()) {
            attachFailureScreenshot(testResult.getMethod().getMethodName());
        }
    }

    private void attachFailureScreenshot(String testName) {
        try {
            WebDriver driver = DriverManager.getDriver();
            if (driver instanceof TakesScreenshot screenshotDriver) {
                byte[] pngBytes = screenshotDriver.getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment(
                        "Failure Screenshot - " + testName,
                        "image/png",
                        new ByteArrayInputStream(pngBytes),
                        ".png"
                );
            }
        } catch (Exception e) {
            System.err.println("Could not capture failure screenshot for " + testName + ": " + e.getMessage());
        }
    }
}
