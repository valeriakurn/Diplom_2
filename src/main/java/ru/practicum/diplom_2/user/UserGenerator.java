package ru.practicum.diplom_2.user;

import com.github.javafaker.Faker;

public class UserGenerator {
    private static Faker faker = new Faker();
    public static User getDefaultUser() {
        return new User(faker.internet().emailAddress(), faker.internet().password(6,10), faker.name().firstName());
    }

    public static User getUserWithoutEmail() {
        return new User(null, faker.internet().password(6,10), faker.name().firstName());
    }

    public static User getUserWithoutPassword() {
        return new User(faker.internet().emailAddress(), null, faker.name().firstName());
    }

    public static User getUserWithoutName() {
        return new User(faker.internet().emailAddress(), faker.internet().password(6,10), null);
    }
}
