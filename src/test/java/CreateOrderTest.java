import com.ya.*;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;

@Epic("Работа с заказами")
@Feature("Создание нового заказа")
@Story("Различные варианты цвета самоката при создании заказа")

@RunWith(Parameterized.class)
public class CreateOrderTest {
    OrderClient orderClient;
    Order order;
    int orderTrack;
    private final String[] colorData;

    public CreateOrderTest(String[] colorData) {
        this.colorData = colorData;
    }

    @Parameterized.Parameters
    public static Object[][] getColorData() {
        return new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"BLACK", "GRAY"}},
                {new String[]{"GRAY"}},
                {new String[]{}},
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderGenerator.getOrderForColorTest(colorData);
    }

    @After
    public void tearDown() {
        orderClient.cancelOrder(orderTrack);
    }

    @Test
    @DisplayName("Создание заказа с различными вариантами цвета самоката")
    @Description("3. Создание заказа:\n  - можно указать один из цветов — BLACK или GREY;\n  - можно указать оба цвета;\n  " +
            "- можно совсем не указывать цвет;\n  - тело ответа содержит track.")
    public void createNewOrderWithDifferentColors() {
        ValidatableResponse createResponse = orderClient.createOrder(order);
        orderTrack = createResponse.extract().path("track"); //если выполняется, значит, тело ответа содержит track
        assertEquals(createResponse.extract().statusCode(), 201);
    }


}
