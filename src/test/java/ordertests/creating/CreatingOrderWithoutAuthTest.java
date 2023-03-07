package ordertests.creating;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import model.order.Order;
import model.order.OrderClient;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;

public class CreatingOrderWithoutAuthTest {
    private OrderClient orderClient;
    private Order order;

    @Before
    public void setUp() {
        order = Order.getOrder();
        orderClient = new OrderClient();
    }

    @Test
    @Tag("Creating an order")
    @Description("Создание заказа:\n" +
            "- без авторизации")
    @DisplayName("Create an order by an unauthorized user")
    public void createOrderWithIngredientWithoutAuth() {
        orderClient.createOrderWithoutAuth(order)
                .assertThat()
                .statusCode(SC_OK);
    }
}
