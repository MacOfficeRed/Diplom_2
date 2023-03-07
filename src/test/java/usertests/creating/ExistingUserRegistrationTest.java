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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ExistingUserRegistrationTest {
    private UserClient userClient;
    private User user;
    private String firstToken;
    private String secondToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = User.getRandomUser();
        ValidatableResponse response = userClient.createUser(user);
        secondToken = response.extract().path("accessToken");
        response.assertThat()
                .statusCode(SC_OK);
    }

    @After
    public void cleanup() {
        if (firstToken != null) {
            userClient.deleteUser(firstToken)
                    .assertThat()
                    .statusCode(SC_ACCEPTED)
                    .and()
                    .body("success", is(true))
                    .and()
                    .body("message", is("User successfully removed"));
        }
        userClient.deleteUser(secondToken)
                .assertThat()
                .statusCode(SC_ACCEPTED);
    }

    @Test
    @Tag("Creating user")
    @Description("Создание пользователя:\n" +
            "создать пользователя, который уже зарегистрирован")
    @DisplayName("Create a user that already exist")
    public void createExistingUser() {
        ValidatableResponse response = userClient.createUser(user);
        if (response.extract().statusCode() == SC_OK) {
            firstToken = response.extract().path("accessToken");
        }
        response.assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("message", equalTo("User already exists"));
    }
}
