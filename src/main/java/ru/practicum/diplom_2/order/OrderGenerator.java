package ru.practicum.diplom_2.order;

public class OrderGenerator {
    public static Order getOrderWithValidIngredients() {
        return new Order(IngredientsGenerator.getValidHashIngredientList());
    }

    public static Order getOrderWithInvalidIngredients() {
        return new Order(IngredientsGenerator.getInvalidHashIngredientList());
    }

    public static Order getOrderWithoutIngredients() {
        return new Order(IngredientsGenerator.getEmptyHashIngredientList());
    }
}
