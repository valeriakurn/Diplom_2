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

public class UpdateUserTest {
    private static User user;
    private static UserClient userClient;
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        user = UserGenerator.getDefaultUser();
        userClient = new UserClient();

        userClient.register(user);
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Authorized user can update email")
    @Description("Verify that in case user is successfully logged in, email can be updated")
    public void EmailCanBeUpdatedByAuthorizedUserTest() {
        ValidatableResponse response = userClient.update(accessToken, UserData.fromUserWithDifferentEmail(user));

        int updateStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, updateStatusCode);

        boolean isUpdated = response.extract().path("success");
        assertTrue("User was not logged in", isUpdated);

        String userEmail = response.extract().path("user.email");
        assertNotEquals("User email is incorrect", user.getEmail(), userEmail);

        String userName = response.extract().path("user.name");
        assertEquals("User name is incorrect", user.getName(), userName);
    }

    @Test
    @DisplayName("Authorized user can update name")
    @Description("Verify that in case user is successfully logged in, name can be updated")
    public void NameCanBeUpdatedByAuthorizedUserTest() {
        ValidatableResponse response = userClient.update(accessToken, UserData.fromUserWithDifferentName(user));
        int updateStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, updateStatusCode);

        boolean isUpdated = response.extract().path("success");
        assertTrue("User was not logged in", isUpdated);

        String userEmail = response.extract().path("user.email");
        assertEquals("User email is incorrect", user.getEmail(), userEmail);

        String userName = response.extract().path("user.name");
        assertNotEquals("User name is incorrect", user.getName(), userName);
    }

    @Test
    @DisplayName("Unauthorized user cannot update email")
    @Description("Verify that in case user is not logged in, email cannot be updated")
    public void EmailCannotBeUpdatedByUnauthorizedUserTest() {
        ValidatableResponse response = userClient.update("", UserData.fromUserWithDifferentEmail(user));

        int updateStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, updateStatusCode);

        boolean isUpdated = response.extract().path("success");
        assertFalse("User email was updated", isUpdated);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "You should be authorised", errorMessage);
    }

    @Test
    @DisplayName("Unauthorized user cannot update name")
    @Description("Verify that in case user is not logged in, name cannot be updated")
    public void NameCannotBeUpdatedByUnauthorizedUserTest() {
        ValidatableResponse response = userClient.update("", UserData.fromUserWithDifferentName(user));

        int updateStatusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, updateStatusCode);

        boolean isUpdated = response.extract().path("success");
        assertFalse("User name was updated", isUpdated);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "You should be authorised", errorMessage);
    }

    @AfterClass
    public static void tearDown() {
        userClient.delete(accessToken);
    }
}
