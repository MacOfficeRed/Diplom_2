package model.user;

import config.Config;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends Config {
    private final String AUTH_REGISTER_PATH = "/auth/register";
    private final String AUTH_LOGIN_PATH = "/auth/login";
    private final String AUTH_USER_PATH = "/auth/user";

    @Step("Create a user")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getSpecification())
                .body(user)
                .when()
                .post(AUTH_REGISTER_PATH)
                .then().log().ifError();
    }

    @Step("User log in")
    public ValidatableResponse logInUser(User user) {
        return given()
                .spec(getSpecification())
                .body(user)
                .when()
                .post(AUTH_LOGIN_PATH)
                .then().log().ifError();
    }

    @Step("Update a user with auth")
    public ValidatableResponse updateUser(User user, String token) {
        return given()
                .spec(getSpecification())
                .header("Authorization", token)
                .body(user)
                .when()
                .patch(AUTH_USER_PATH)
                .then().log().ifError();
    }

    @Step("Update a user without auth")
    public ValidatableResponse updateUserWithoutAuth(User user) {
        return given()
                .spec(getSpecification())
                .body(user)
                .when()
                .patch(AUTH_USER_PATH)
                .then().log().ifError();
    }

    @Step("Delete a user")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getSpecification())
                .header("Authorization", token)
                .when()
                .delete(AUTH_USER_PATH)
                .then().log().ifError();
    }
}
