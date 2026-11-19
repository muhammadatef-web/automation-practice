package tests.api;

import core.api.AccountApiClient;
import core.api.ApiSpecFactory;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;
import testdata.UserFactory;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

@Epic("REST API")
@Feature("Catalog & Account Endpoints")
public class ProductsAndAuthApiTest {

    @Test(groups = {"smoke", "regression"})
    @Story("Products Catalog API")
    @Severity(SeverityLevel.CRITICAL)
    @Description("GET /api/productsList returns 200 and a non-empty list of products with nested category metadata.")
    public void verifyGetAllProductsList() {
        Response response = RestAssured.given()
                .spec(ApiSpecFactory.defaultSpec())
        .when()
                .get("/api/productsList")
        .then()
                .spec(ApiSpecFactory.jsonResponseSpec())
                .statusCode(200)
                .body("responseCode", equalTo(200))
                .body("products", not(empty()))
                .body("products[0].id", notNullValue())
                .body("products[0].category.usertype.usertype", notNullValue())
                .extract()
                .response();

        List<String> productNames = response.jsonPath().getList("products.name");
        Assert.assertTrue(productNames.contains("Blue Top"), "Catalog should include 'Blue Top'");
    }

    @Test(groups = {"regression"})
    @Story("Product Search API")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /api/searchProduct filters catalog items matching the search keyword.")
    public void verifySearchProductEndpoint() {
        Response response = RestAssured.given()
                .spec(ApiSpecFactory.formUrlEncodedSpec())
                .formParam("search_product", "top")
        .when()
                .post("/api/searchProduct")
        .then()
                .spec(ApiSpecFactory.jsonResponseSpec())
                .statusCode(200)
                .body("responseCode", equalTo(200))
                .body("products", not(empty()))
                .extract()
                .response();

        int matchedCount = response.jsonPath().getList("products").size();
        Assert.assertTrue(matchedCount >= 1, "Expected at least 1 product matching 'top'");
    }

    @Test(groups = {"regression"})
    @Story("Authentication API")
    @Severity(SeverityLevel.NORMAL)
    @Description("POST /api/verifyLogin returns inner responseCode 404 when credentials do not exist.")
    public void verifyLoginWithUnregisteredCredentialsReturns404() {
        var fakeUser = UserFactory.unregisteredUser();

        RestAssured.given()
                .spec(ApiSpecFactory.formUrlEncodedSpec())
                .formParams(Map.of(
                        "email", fakeUser.email(),
                        "password", fakeUser.password()
                ))
        .when()
                .post("/api/verifyLogin")
        .then()
                .spec(ApiSpecFactory.jsonResponseSpec())
                .statusCode(200)
                .body("responseCode", equalTo(404))
                .body("message", equalTo("User not found!"));
    }

    @Test(groups = {"smoke", "regression"})
    @Story("Account CRUD Lifecycle")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Create user via POST, verify via POST /verifyLogin, read via GET, update city via PUT, and delete via DELETE.")
    public void verifyFullAccountCrudLifecycle() {
        var user = UserFactory.freshValidUser();
        AccountApiClient.createAccount(user.name(), user.email(), user.password());

        try {
            // 1. Verify login via API
            RestAssured.given()
                    .spec(ApiSpecFactory.formUrlEncodedSpec())
                    .formParams(Map.of("email", user.email(), "password", user.password()))
            .when()
                    .post("/api/verifyLogin")
            .then()
                    .spec(ApiSpecFactory.jsonResponseSpec())
                    .statusCode(200)
                    .body("responseCode", equalTo(200))
                    .body("message", equalTo("User exists!"));

            // 2. Update user profile via PUT
            RestAssured.given()
                    .spec(ApiSpecFactory.formUrlEncodedSpec())
                    .formParams(Map.ofEntries(
                            Map.entry("name", user.name()),
                            Map.entry("email", user.email()),
                            Map.entry("password", user.password()),
                            Map.entry("title", "Mr"),
                            Map.entry("birth_date", "15"),
                            Map.entry("birth_month", "June"),
                            Map.entry("birth_year", "1994"),
                            Map.entry("firstname", "Muhammad"),
                            Map.entry("lastname", "QA"),
                            Map.entry("company", "AutomationLab"),
                            Map.entry("address1", "123 Testing Street"),
                            Map.entry("address2", "Suite 4"),
                            Map.entry("country", "United States"),
                            Map.entry("zipcode", "90001"),
                            Map.entry("state", "New York"),
                            Map.entry("city", "New York City"),
                            Map.entry("mobile_number", "+1234567890")
                    ))
            .when()
                    .put("/api/updateAccount")
            .then()
                    .spec(ApiSpecFactory.jsonResponseSpec())
                    .statusCode(200)
                    .body("responseCode", equalTo(200))
                    .body("message", equalTo("User updated!"));

            // 3. Query updated profile via GET + queryParam
            RestAssured.given()
                    .spec(ApiSpecFactory.defaultSpec())
                    .queryParam("email", user.email())
            .when()
                    .get("/api/getUserDetailByEmail")
            .then()
                    .spec(ApiSpecFactory.jsonResponseSpec())
                    .statusCode(200)
                    .body("responseCode", equalTo(200))
                    .body("user.email", equalTo(user.email()))
                    .body("user.city", equalTo("New York City"));
        } finally {
            AccountApiClient.deleteAccount(user.email(), user.password());
        }
    }
}