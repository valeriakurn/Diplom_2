package ru.practicum.diplom_2.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.practicum.diplom_2.user.*;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class GetOrdersTest {
    private static OrderClient orderClient;
    private static User user;
    private static UserClient userClient;
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        orderClient = new OrderClient();

        user = UserGenerator.getDefaultUser();
        userClient = new UserClient();

        userClient.register(user);
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Authorized user can get list of orders")
    @Description("Verify that in case user is successfully logged in, list of orders can be returned")
    public void OrdersListCanBeReceivedByAuthorizedUserTest() {
        ValidatableResponse response = orderClient.getOrders(accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean isReceived = response.extract().path("success");
        assertTrue("List of orders was not returned", isReceived);
    }

    @Test
    @DisplayName("Unauthorized user cannot get list of orders")
    @Description("Verify that in case user is not logged in, list of orders cannot be returned")
    public void OrdersListCannotBeReceivedByUnauthorizedUserTest() {
        ValidatableResponse response = orderClient.getOrders("");

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);

        boolean isReceived = response.extract().path("success");
        assertFalse("List if orders was received", isReceived);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "You should be authorised", errorMessage);
    }

    @AfterClass
    public static void tearDown() {
        userClient.delete(accessToken);
    }

}
