import com.ya.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

//Так как в задании ничего не сказано про то, что нужно проверить работу запроса с различными параметрами,
//то будет проведена проверка запроса по умолчанию.
@Epic("Работа с заказами")
@Feature("Получение списка заказов")
public class GetOrderListTest {
    OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка из 30 заказов (по умолчанию)")
    @Description("4. Список заказов:\n Проверь, что в тело ответа возвращается список заказов.")
    public void getOrderListTest() {
        ValidatableResponse getOrderList = orderClient.getAvailableOrdersList();
        ArrayList responseBody = getOrderList.extract().path("orders");
        assertEquals(responseBody.size(), 30);
        assertEquals(getOrderList.extract().statusCode(), 200);
    }
}
