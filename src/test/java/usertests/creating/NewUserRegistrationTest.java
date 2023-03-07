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
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;

public class NewUserRegistrationTest {
    private UserClient userClient;
    private User user;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
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
    @Tag("Creating user")
    @Description("Создание пользователя:\n" +
            "создать уникального пользователя")
    @DisplayName("Create a user positive test")
    public void createUniqueUser() {
        user = User.getRandomUser();
        ValidatableResponse response = userClient.createUser(user);
        token = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(SC_OK);
    }
}
