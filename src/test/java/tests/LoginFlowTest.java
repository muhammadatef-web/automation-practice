package tests;

import core.api.AccountApiClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.LoginPage;
import testdata.UserFactory;

@Epic("Authentication")
@Feature("User Login")
public class LoginFlowTest extends BaseTest {

    @Test(groups = {"regression"})
    @Story("Invalid Credentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that submitting a wrong password displays the authentication error banner.")
    public void verifyInvalidPasswordShowsError() {
        var user = UserFactory.invalidPasswordUser();

        LoginPage loginPage = new LoginPage()
                .openLoginPage()
                .loginAs(user.email(), user.password());

        Assert.assertEquals(
                loginPage.getLoginErrorMessage(),
                "Your email or password is incorrect!",
                "Expected invalid credentials banner was not displayed"
        );
    }

    @Test(groups = {"regression"})
    @Story("Invalid Credentials")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an unregistered email address cannot log in.")
    public void verifyUnregisteredEmailShowsError() {
        var user = UserFactory.unregisteredUser();

        LoginPage loginPage = new LoginPage()
                .openLoginPage()
                .loginAs(user.email(), user.password());

        Assert.assertEquals(
                loginPage.getLoginErrorMessage(),
                "Your email or password is incorrect!",
                "Expected invalid credentials banner was not displayed"
        );
    }

    @Test(groups = {"smoke", "regression"})
    @Story("Valid Login")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Seed a fresh user via REST API, log in through the UI, verify navbar state, and delete the user.")
    public void verifyValidUserCanLogin() {
        var user = UserFactory.freshValidUser();
        AccountApiClient.createAccount(user.name(), user.email(), user.password());

        try {
            LoginPage loginPage = new LoginPage()
                    .openLoginPage()
                    .loginAs(user.email(), user.password());

            SoftAssert softAssert = new SoftAssert();
            softAssert.assertTrue(
                    loginPage.isUserLoggedIn(),
                    "Logout link should be visible after valid login"
            );
            softAssert.assertTrue(
                    loginPage.getLoggedInUserText().contains(user.name()),
                    "Navbar should display 'Logged in as " + user.name() + "'"
            );
            softAssert.assertAll();
        } finally {
            AccountApiClient.deleteAccount(user.email(), user.password());
        }
    }
}