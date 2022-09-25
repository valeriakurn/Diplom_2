package ru.practicum.diplom_2.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class LoginUserTest {
    private static User user;
    private static UserClient userClient;
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        user = UserGenerator.getDefaultUser();
        userClient = new UserClient();

        userClient.register(user);
    }

    @Test
    @DisplayName("User can be logged in")
    @Description("Verify that in case user is successfully logged in, expected status code and response body will be received")
    public void UserCanBeLoggedInTest() {
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        int loginStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, loginStatusCode);

        boolean isLoggedIn = response.extract().path("success");
        assertTrue("User was not logged in", isLoggedIn);

        String userEmail = response.extract().path("user.email");
        assertEquals("User email is incorrect", user.getEmail(), userEmail);

        String userName = response.extract().path("user.name");
        assertEquals("User name is incorrect", user.getName(), userName);

        accessToken = response.extract().path("accessToken");
        assertNotNull("Access token wasn't returned", accessToken);

        String refreshToken = response.extract().path("refreshToken");
        assertNotNull("Refresh token wasn't returned", refreshToken);
    }

    @Test
    @DisplayName("User cannot be logged in using invalid email")
    @Description("Verify that in case user is tried to logged in using invalid email, expected status code and message will be received")
    public void UserCannotBeLoggedInWithInvalidEmailTest() {
        ValidatableResponse response = userClient.login(UserCredentials.fromUserWithIncorrectEmail(user));
        int loginStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, loginStatusCode);

        boolean isLoggedIn = response.extract().path("success");
        assertFalse("User was logged in", isLoggedIn);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "email or password are incorrect", errorMessage);
    }

    @Test
    @DisplayName("User cannot be logged in using invalid password")
    @Description("Verify that in case user is tried to logged in using invalid password, expected status code and message will be received")
    public void UserCannotBeLoggedInWithInvalidPasswordTest() {
        ValidatableResponse response = userClient.login(UserCredentials.fromUserWithIncorrectPassword(user));
        int loginStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, loginStatusCode);

        boolean isLoggedIn = response.extract().path("success");
        assertFalse("User was logged in", isLoggedIn);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "email or password are incorrect", errorMessage);
    }

    @Test
    @DisplayName("User cannot be logged in without email")
    @Description("Verify that in case user is tried to logged in without email, expected status code and message will be received")
    public void UserCannotBeLoggedInWithoutEmailTest() {
        ValidatableResponse response = userClient.login(UserCredentials.fromUserWithoutEmail(user));
        int loginStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, loginStatusCode);

        boolean isLoggedIn = response.extract().path("success");
        assertFalse("User was logged in", isLoggedIn);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "email or password are incorrect", errorMessage);
    }

    @Test
    @DisplayName("User cannot be logged in without password")
    @Description("Verify that in case user is tried to logged in without password, expected status code and message will be received")
    public void UserCannotBeLoggedInWithoutPasswordTest() {
        ValidatableResponse response = userClient.login(UserCredentials.fromUserWithoutPassword(user));
        int loginStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, loginStatusCode);

        boolean isLoggedIn = response.extract().path("success");
        assertFalse("User was logged in", isLoggedIn);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "email or password are incorrect", errorMessage);
    }

    @AfterClass
    public static void tearDown() {
        userClient.delete(accessToken);
    }

}
