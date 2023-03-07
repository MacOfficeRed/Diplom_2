package ordertests.getting;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.junit4.Tag;
import model.order.OrderClient;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class GettingOrderWithoutAuthTest {
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @Tag("Getting an order")
    @Description("Получение заказов конкретного пользователя:\n" +
            "- неавторизованный пользователь")
    @DisplayName("Get an order by an unauthorized user")
    public void getOrderWithoutAuth() {
        orderClient.getOrderWithoutAuth()
                .assertThat()
                .statusCode(SC_UNAUTHORIZED);
    }
}
