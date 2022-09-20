package ru.practicum.diplom_2.order;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.practicum.diplom_2.user.User;
import ru.practicum.diplom_2.user.UserClient;
import ru.practicum.diplom_2.user.UserCredentials;
import ru.practicum.diplom_2.user.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateOrderTest {
    private static Order orderValidIngredients;
    private static Order orderInvalidIngredients;
    private static Order orderWithoutIngredients;
    private static OrderClient orderClient;
    private static User user;
    private static UserClient userClient;
    private static String accessToken;

    @BeforeClass
    public static void setUp() {
        orderValidIngredients = OrderGenerator.getOrderWithValidIngredients();
        orderInvalidIngredients = OrderGenerator.getOrderWithInvalidIngredients();
        orderWithoutIngredients = OrderGenerator.getOrderWithoutIngredients();
        orderClient = new OrderClient();

        user = UserGenerator.getDefaultUser();
        userClient = new UserClient();

        userClient.register(user);
        ValidatableResponse response = userClient.login(UserCredentials.from(user));
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("Authorized user can create order")
    @Description("Verify that in case user is logged in and valid list of ingrediends is sent, order can be created")
    public void OrderCanBeCreatedByAuthorizedUserTest() {
        ValidatableResponse response = orderClient.create(orderValidIngredients, accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_OK, statusCode);

        boolean isCreated = response.extract().path("success");
        assertTrue("Order was not created", isCreated);

        String name = response.extract().path("name");
        assertNotNull("Name wasn't returned", name);

        int orderNumber = response.extract().path("order.number");
        assertNotNull("Order number wasn't returned", orderNumber);
    }

    @Test
    @DisplayName("Unauthorized user cannot create order")
    @Description("Verify that in case user is not logged in, order cannot be created")
    public void OrderCannotBeCreatedByUnauthorizedUserTest() {
        ValidatableResponse response = orderClient.create(orderValidIngredients, "");

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_UNAUTHORIZED, statusCode);

        boolean isCreated = response.extract().path("success");
        assertFalse("Order was created", isCreated);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "You should be authorised", errorMessage);
    }

    @Test
    @DisplayName("Order with invalid ingredients cannot be created")
    @Description("Verify that in case invalid hash of ingredients is provided, order cannot be created")
    public void OrderWithInvalidIngredientsCannotBeCreatedTest() {
        ValidatableResponse response = orderClient.create(orderInvalidIngredients, accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_INTERNAL_SERVER_ERROR, statusCode);
    }

    @Test
    @DisplayName("Order without ingredients cannot be created")
    @Description("Verify that in case ingredients are not provided, order cannot be created")
    public void OrderWithoutIngredientsCannotBeCreatedTest() {
        ValidatableResponse response = orderClient.create(orderWithoutIngredients, accessToken);

        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrect", SC_BAD_REQUEST, statusCode);

        String errorMessage = response.extract().path("message");
        assertEquals("Incorrect error message was received", "Ingredient ids must be provided", errorMessage);
    }

    @AfterClass
    public static void tearDown() {
        userClient.delete(accessToken);
    }
}
