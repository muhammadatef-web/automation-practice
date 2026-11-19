package core.api;

import core.config.ConfigReader;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public final class ApiSpecFactory {

    static {
        registerJsonParsers();
    }

    private ApiSpecFactory() {}

    private static void registerJsonParsers() {
        RestAssured.registerParser("text/html", Parser.JSON);
        RestAssured.registerParser("text/plain", Parser.JSON);
        RestAssured.defaultParser = Parser.JSON;
    }

    public static RequestSpecification defaultSpec() {
        registerJsonParsers();
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("base.url"))
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36")
                .addHeader("Accept", "application/json, text/plain, */*")
                .addFilter(new AllureRestAssured())
                .log(LogDetail.METHOD)
                .log(LogDetail.URI)
                .build();
    }

    public static RequestSpecification formUrlEncodedSpec() {
        return new RequestSpecBuilder()
                .addRequestSpecification(defaultSpec())
                .setContentType(ContentType.URLENC)
                .build();
    }

    public static ResponseSpecification jsonResponseSpec() {
        return new ResponseSpecBuilder()
                .registerParser("text/html", Parser.JSON)
                .registerParser("text/plain", Parser.JSON)
                .setDefaultParser(Parser.JSON)
                .build();
    }
}
