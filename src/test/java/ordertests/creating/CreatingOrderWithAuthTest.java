package ordertests.creating;

import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import io.restassured.response.ValidatableResponse;
import model.order.Order;
import model.order.OrderClient;
import model.user.User;
import model.user.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.is;

public class CreatingOrderWithAuthTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String token;
    private Order order;
    private Faker faker;

    @Before
    public void setUp() {
        faker = new Faker();
        order = Order.getOrder();
        orderClient = new OrderClient();
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
                .body("message", is("User successfully removed"))
                .and()
                .body("success", is(true));
    }

    @Test
    @Tag("Creating an order")
    @Description("Создание заказа:\n" +
            "- с авторизацией")
    @DisplayName("Create an order with ingredients by an authorized user")
    public void createOrderWithIngredientsWithAuth() {
        orderClient.createOrderWithAuth(order, token)
                .assertThat()
                .statusCode(SC_OK);
    }

    @Test
    @Tag("Creating an order")
    @Description("Создание заказа:\n" +
            "- с неверным хешем ингредиентов")
    @DisplayName("Create an order with non valid ingredients hash by an authorized user")
    public void createOrderWithNonValidIngredientsHash() {
        order.setIngredients(new String[]{faker.internet().uuid(), faker.internet().uuid(), faker.internet().uuid()});
        orderClient.createOrderWithAuth(order, token)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @Tag("Creating an order")
    @Description("Создание заказа:\n" +
            "- без ингредиентов")
    @DisplayName("Create an order without any ingredients an authorized user")
    public void createOrderWithoutIngredients() {
        order.setIngredients(null);
        orderClient.createOrderWithAuth(order, token)
                .assertThat()
                .statusCode(SC_BAD_REQUEST);
    }
}
