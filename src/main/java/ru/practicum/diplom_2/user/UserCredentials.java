package ru.practicum.diplom_2.user;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserCredentials {
    private String email;
    private String password;
    private static Faker faker = new Faker();

    public static UserCredentials from(User user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }

    public static UserCredentials fromUserWithIncorrectEmail(User user) {
        return new UserCredentials(faker.internet().emailAddress(), user.getPassword());
    }

    public static UserCredentials fromUserWithIncorrectPassword(User user) {
        return new UserCredentials(user.getEmail(), faker.internet().password(6,10));
    }

    public static UserCredentials fromUserWithoutEmail(User user) {
        return new UserCredentials(null, user.getPassword());
    }

    public static UserCredentials fromUserWithoutPassword(User user) {
        return new UserCredentials(user.getEmail(), null);
    }

}
