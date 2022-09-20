package ru.practicum.diplom_2.user;

import io.restassured.response.ValidatableResponse;
import ru.practicum.diplom_2.RestClient;

import static io.restassured.RestAssured.given;

public class UserClient extends RestClient {
    private static final String REGISTER_PATH = "/api/auth/register";
    private static final String LOGIN_PATH = "/api/auth/login";
    private static final String USER_PATH = "/api/auth/user ";

    public ValidatableResponse register(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(REGISTER_PATH)
                .then();
    }

    public ValidatableResponse login(UserCredentials userCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(userCredentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    public ValidatableResponse delete(String accessToken) {
        if (accessToken == null) {
            accessToken = "";
        }

        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_PATH)
                .then();
    }

    public ValidatableResponse get(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .get(USER_PATH)
                .then();
    }

    public ValidatableResponse update(String accessToken, UserData userData) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(userData)
                .when()
                .patch(USER_PATH)
                .then();
    }

}
