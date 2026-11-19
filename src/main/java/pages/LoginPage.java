package pages;

import core.ui.ElementActions;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class LoginPage {

    private final ElementActions ui = new ElementActions();

    private final By signupLoginNavLink = By.cssSelector("a[href='/login']");
    private final By loginEmailInput = By.cssSelector("input[data-qa='login-email']");
    private final By loginPasswordInput = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");

    private final By logoutNavLink = By.cssSelector("a[href='/logout']");
    private final By loggedInAsLabel = By.xpath("//a[contains(.,'Logged in as')]");
    private final By loginErrorMessage = By.cssSelector(".login-form p");

    @Step("Open Signup / Login page from top navigation bar")
    public LoginPage openLoginPage() {
        ui.click(signupLoginNavLink);
        return this;
    }

    @Step("Submit login credentials for email: {email}")
    public LoginPage loginAs(String email, String password) {
        ui.type(loginEmailInput, email);
        ui.type(loginPasswordInput, password);
        ui.click(loginButton);
        return this;
    }

    @Step("Check if Logout link is visible in navigation bar")
    public boolean isUserLoggedIn() {
        return ui.isDisplayed(logoutNavLink);
    }

    @Step("Read logged-in username banner")
    public String getLoggedInUserText() {
        return ui.getText(loggedInAsLabel);
    }

    @Step("Read login validation error message")
    public String getLoginErrorMessage() {
        return ui.getText(loginErrorMessage);
    }
}
