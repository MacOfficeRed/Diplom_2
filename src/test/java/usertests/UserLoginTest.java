package usertests;

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

public class UserLoginTest {
    private UserClient userClient;
    private User user;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        ValidatableResponse response = userClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(SC_OK);
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
    @Tag("User log in")
    @Description("Логин пользователя:\n" +
            "логин под существующим пользователем")
    @DisplayName("Log in by valid user")
    public void logInByValidUser() {
        userClient.logInUser(user)
                .assertThat()
                .statusCode(SC_OK);
    }

    @Test
    @Tag("User log in")
    @Description("Логин пользователя:\n" +
            "логин с некорректным паролем")
    @DisplayName("Log in with invalid password")
    public void logInWithInvalidPassword() {
        user.setPassword("12345");
        userClient.logInUser(user)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Tag("User log in")
    @Description("Логин пользователя:\n" +
            "логин с некорректной эл.почтой")
    @DisplayName("Log in with invalid email")
    public void logInWithInvalidEmail() {
        user.setEmail("12345");
        userClient.logInUser(user)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
