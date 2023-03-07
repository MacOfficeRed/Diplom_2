package model.order;

import config.Config;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Config {
    public final String ORDERS_PATH = "/orders";

    @Step("Create an order with auth")
    public ValidatableResponse createOrderWithAuth(Order order, String token) {
        return given()
                .spec(getSpecification())
                .header("Authorization", token)
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then().log().ifError();
    }

    @Step("Create an order without auth")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .spec(getSpecification())
                .body(order)
                .when()
                .post(ORDERS_PATH)
                .then().log().ifError();
    }

    @Step("Get an order with auth")
    public ValidatableResponse getOrderWithAuth(String token) {
        return given()
                .spec(getSpecification())
                .header("Authorization", token)
                .when()
                .get(ORDERS_PATH)
                .then().log().ifError();
    }

    @Step("Get an order without auth")
    public ValidatableResponse getOrderWithoutAuth() {
        return given()
                .spec(getSpecification())
                .when()
                .get(ORDERS_PATH)
                .then().log().ifError();
    }
}
