package core.api;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public final class AccountApiClient {

    private AccountApiClient() {}

    @Step("API Setup: Create test user account for {email}")
    public static void createAccount(String name, String email, String password) {
        RestAssured.given()
                .spec(ApiSpecFactory.formUrlEncodedSpec())
                .formParams(Map.ofEntries(
                        Map.entry("name", name),
                        Map.entry("email", email),
                        Map.entry("password", password),
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
                        Map.entry("city", "Los Angeles"),
                        Map.entry("mobile_number", "+1234567890")
                ))
        .when()
                .post("/api/createAccount")
        .then()
                .spec(ApiSpecFactory.jsonResponseSpec())
                .statusCode(200)
                .body("responseCode", equalTo(201));
    }

    @Step("API Cleanup: Delete test user account for {email}")
    public static void deleteAccount(String email, String password) {
        RestAssured.given()
                .spec(ApiSpecFactory.formUrlEncodedSpec())
                .formParams(Map.of(
                        "email", email,
                        "password", password
                ))
        .when()
                .delete("/api/deleteAccount")
        .then()
                .spec(ApiSpecFactory.jsonResponseSpec())
                .statusCode(200)
                .body("responseCode", equalTo(200));
    }
}
