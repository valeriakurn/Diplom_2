package ru.practicum.diplom_2.user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateUserTest {
    private static User user;
    private static User userWithoutEmail;
    private static User userWithoutPassword;
    private static User userWithoutName;
    private static UserClient userClient;
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        user = UserGenerator.getDefaultUser();
        userWithoutEmail = UserGenerator.getUserWithoutEmail();
        userWithoutPassword = UserGenerator.getUserWithoutPassword();
        userWithoutName = UserGenerator.getUserWithoutName();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("User can be registered")
    @Description("Verify that in case user is successfully registered, expected status code and response body will be received")
    public void UserCanBeCreatedTest() {
        ValidatableResponse response = userClient.register(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean isRegistered = response.extract().path("success");
        assertTrue("User was not registered", isRegistered);

        String userEmail = response.extract().path("user.email");
        assertEquals("User email is incorrect", user.getEmail(), userEmail);

        String userName = response.extract().path("user.name");
        assertEquals("User name is incorrect", user.getName(), userName);

        accessToken = response.extract().path("accessToken");
        assertNotNull("Access token wasn't returned", accessToken);

        String refreshToken = response.extract().path("refreshToken");
        assertNotNull("Refresh token wasn't returned", refreshToken);

        ValidatableResponse loginResponse = userClient.login(UserCredentials.from(user));
        boolean isLoggedIn = loginResponse.extract().path("success");
        assertTrue("User was not logged in", isLoggedIn);
    }

    @Test
    @DisplayName("Two identical users cannot be registered")
    @Description("Verify that in case existed user is tried to be registered again, expected status code and error message will be received")
    public void TwoIdenticalUsersCannotBeRegisteredTest() {
        ValidatableResponse response = userClient.register(user);
        accessToken = response.extract().path("accessToken");
        assertNotNull("Access token wasn't returned", accessToken);

        response = userClient.register(user);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);

        boolean isCreated = response.extract().path("success");
        assertFalse("User was created", isCreated);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "User already exists", errorMessage);
    }

    @Test
    @DisplayName("User cannot be created without email")
    @Description("Verify that in case user is tried to be registered without email, expected status code and error message will be received")
    public void UserWithoutEmailCannotBeCreatedTest() {
        ValidatableResponse response = userClient.register(userWithoutEmail);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);

        boolean isCreated = response.extract().path("success");
        assertFalse("User was created", isCreated);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "Email, password and name are required fields", errorMessage);
    }

    @Test
    @DisplayName("User cannot be created without password")
    @Description("Verify that in case user is tried to be registered without password, expected status code and error message will be received")
    public void UserWithoutPasswordCannotBeCreatedTest() {
        ValidatableResponse response = userClient.register(userWithoutPassword);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);

        boolean isCreated = response.extract().path("success");
        assertFalse("User was created", isCreated);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "Email, password and name are required fields", errorMessage);
    }

    @Test
    @DisplayName("User cannot be created without name")
    @Description("Verify that in case user is tried to be registered without name, expected status code and error message will be received")
    public void UserWithoutNameCannotBeCreatedTest() {
        ValidatableResponse response = userClient.register(userWithoutName);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_FORBIDDEN, statusCode);

        boolean isCreated = response.extract().path("success");
        assertFalse("User was created", isCreated);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "Email, password and name are required fields", errorMessage);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }
}
