package ru.practicum.diplom_2.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.javafaker.Faker;

public class IngredientsGenerator {
    private static Faker faker = new Faker();

    public static ArrayList<String> getValidHashIngredientList() {
        OrderClient orderClient = new OrderClient();
        Ingredients ingredients = orderClient.getIngredients();

        List<Data> data = ingredients.getData();
        ArrayList<String> ingredientsId = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            ingredientsId.add(data.get(i).get_id());
        }

        Random random = new Random();
        String hashId = ingredientsId.get(random.nextInt(ingredientsId.size()));

        ArrayList<String> validHashIngredient = new ArrayList<>();
        validHashIngredient.add(hashId);
        return validHashIngredient;
    }

    public static ArrayList<String> getInvalidHashIngredientList() {
        ArrayList<String> invalidHashIngredient = new ArrayList<>();
        invalidHashIngredient.add(faker.regexify("[A-Za-z0-9]{20}"));
        return invalidHashIngredient;
    }

    public static ArrayList<String> getEmptyHashIngredientList() {
        ArrayList<String> emptyHashIngredient = new ArrayList<>();
        return emptyHashIngredient;
    }
}
