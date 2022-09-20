package ru.practicum.diplom_2.order;

import io.restassured.response.ValidatableResponse;
import ru.practicum.diplom_2.RestClient;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {
    private static final String ORDER_PATH = "/api/orders";
    private static final String INGREDIENTS_PATH = "/api/ingredients";

    public ValidatableResponse create(Order order, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    public ValidatableResponse getOrders(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .get(ORDER_PATH)
                .then();
    }

    public Ingredients getIngredients() {
        return given()
                .spec(getBaseSpec())
                .get(INGREDIENTS_PATH)
                .body()
                .as(Ingredients.class);
    }

}
