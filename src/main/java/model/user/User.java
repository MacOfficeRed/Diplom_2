package model.user;

import com.github.javafaker.Faker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private String email;
    private String password;
    private String name;

    public static User getRandomUser() {
        Faker faker = new Faker();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().username();
        return new User(email, password, name);
    }
}
