package ru.practicum.diplom_2.user;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserData {
    private String email;
    private String name;
    private static Faker faker = new Faker();

    public static UserData from(User user) {
        return new UserData(user.getEmail(), user.getName());
    }

    public static UserData fromUserWithDifferentEmail(User user) {
        return new UserData(faker.internet().emailAddress(), user.getName());
    }

    public static UserData fromUserWithDifferentName(User user) {
        return new UserData(user.getEmail(), faker.name().firstName());
    }

}
