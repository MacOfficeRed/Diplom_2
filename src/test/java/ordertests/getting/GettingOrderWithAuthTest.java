package ordertests.getting;

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

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;

public class GettingOrderWithAuthTest {
    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String token;
    private Order order;

    @Before
    public void setUp() {
        order = Order.getOrder();
        orderClient = new OrderClient();
        userClient = new UserClient();
        user = User.getRandomUser();
        ValidatableResponse response = userClient.createUser(user);
        token = response.extract().path("accessToken");

        response.assertThat()
                .statusCode(SC_OK);
        orderClient.createOrderWithAuth(order, token)
                .assertThat()
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
    @Tag("Getting an order")
    @Description("Получение заказов конкретного пользователя:\n" +
            "- авторизованный пользователь")
    @DisplayName("Get an order by an authorized user")
    public void getOrderWithAuth() {
        orderClient.getOrderWithAuth(token)
                .assertThat()
                .statusCode(SC_OK);
    }
}
