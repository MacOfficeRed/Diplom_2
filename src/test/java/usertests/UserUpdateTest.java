package usertests;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import model.user.User;
import model.user.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserUpdateTest {
    private UserClient userClient;
    private User user;
    private String token;
    private Faker faker;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        faker = new Faker();
        ValidatableResponse response = userClient.createUser(user);
        response.assertThat().statusCode(SC_OK);
        token = response.extract().path("accessToken");
    }

    @After
    public void cleanup() {
        userClient.deleteUser(token)
                .assertThat()
                .statusCode(SC_ACCEPTED)
                .and()
                .body("success", is(true))
                .and()
                .body("message", is("User successfully removed"));
    }

    @Test
    @Tag("User update")
    @Description("Изменение данных пользователя:\n" +
            "с авторизацией: имя пользователя")
    @DisplayName("Update a user name")
    public void updateUserName() {
        String name = faker.name().username();
        user.setName(name);
        userClient.updateUser(user, token)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.name", equalTo(name));
    }

    @Test
    @Tag("User update")
    @Description("Изменение данных пользователя:\n" +
            "с авторизацией: эл.почта")
    @DisplayName("Update an email")
    public void updateUserEmail() {
        String email = faker.internet().emailAddress();
        user.setEmail(email);
        userClient.updateUser(user, token)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("user.email", equalTo(email));
    }

    @Test
    @Tag("User update")
    @Description("Изменение данных пользователя:\n" +
            "без авторизации")
    @DisplayName("Update a user data without auth")
    public void updateUserWithoutAuth() {
        userClient.updateUserWithoutAuth(user)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
