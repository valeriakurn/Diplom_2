package ru.practicum.diplom_2;

import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class RestClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final Header BASE_CONTENT_TYPE = new Header("Content-type", "application/json");

    public RequestSpecification getBaseSpec() {
        return given()
                .baseUri(BASE_URL)
                .header(BASE_CONTENT_TYPE);
    }
}
