package core.ui;

import core.driver.DriverManager;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementActions {

    private WebDriverWait getWait() {
        return new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(10));
    }

    public void click(By locator) {
        getWait().until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void scrollAndClick(By locator) {
        WebElement element = getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) DriverManager.getDriver())
                .executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        getWait().until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void type(By locator, String text) {
        WebElement element = getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    public String getText(By locator) {
        return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    public List<String> getAllTexts(By locator) {
        getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
        return DriverManager.getDriver().findElements(locator).stream()
                .map(WebElement::getText)
                .toList();
    }

    public int getCount(By locator) {
        getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
        return DriverManager.getDriver().findElements(locator).size();
    }

    public boolean isDisplayed(By locator) {
        try {
            return getWait().until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}