package usertests.creating;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import model.user.User;
import model.user.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserRegistrationNegativeTest {
    private UserClient userClient;
    private User user;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void cleanup() {
        if (token != null) {
            userClient.deleteUser(token)
                    .assertThat()
                    .statusCode(SC_ACCEPTED)
                    .and()
                    .body("success", is(true))
                    .and()
                    .body("message", is("User successfully removed"));
        }
    }

    @Test
    @Tag("Creating user")
    @Description("Создание пользователя:\n" +
            "создать пользователя и не заполнить одно из обязательных полей: пароль")
    @DisplayName("Create a user without password")
    public void createUserWithoutPassword() {
        user = new User("12345", "", "12345");
        ValidatableResponse response = userClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Tag("Creating user")
    @Description("Создание пользователя:\n" +
            "создать пользователя и не заполнить одно из обязательных полей: эл.почта")
    @DisplayName("Create a user without email")
    public void createUserWithoutEmail() {
        user = new User("", "12345", "12345");
        ValidatableResponse response = userClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Tag("Creating user")
    @Description("Создание пользователя:\n" +
            "создать пользователя и не заполнить одно из обязательных полей: имя")
    @DisplayName("Create a user without name")
    public void createUserWithoutName() {
        user = new User("12345", "12345", "");
        ValidatableResponse response = userClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("Email, password and name are required fields"));
    }
}
