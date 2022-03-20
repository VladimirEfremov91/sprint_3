import com.ya.Courier;
import com.ya.CourierClient;
import com.ya.CourierCredentials;
import com.ya.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

@Epic("Работа с курьерами")
@Feature("Создание курьера")
public class CreateCourierTest {

    CourierClient courierClient;
    CourierCredentials credentials;
    Courier courier;
    int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
    }

    @After
    public void tearDown() {
        ValidatableResponse courierCheck = courierClient.loginForGetId(credentials);
        int statusCode = courierCheck.extract().statusCode();
        if (statusCode == 200) {
            courierId = courierCheck.extract().path("id");
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("1. Создание курьера:\n - курьера можно создать;\n - запрос возвращает правильный код ответа;\n - успешный запрос возвращает ok: true.")
    public void courierCanBeCreated() {
        ValidatableResponse createResponse = courierClient.createCourier(new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()));
        assertEquals(createResponse.extract().body().asString(), "{\"ok\":true}");
        assertEquals(createResponse.extract().statusCode(), 201);
    }

    @Test
    @DisplayName("Создание курьера с использованием только обязательных полей")
    @Description("1. Создание курьера:\n - чтобы создать курьера, нужно передать в ручку все обязательные поля.")
    public void createCourierWithoutFirstName() {
        Courier courierWithoutFirstName = Courier.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();
        ValidatableResponse createResponse = courierClient.createCourier(courierWithoutFirstName);
        assertEquals(createResponse.extract().body().asString(), "{\"ok\":true}");
        assertEquals(createResponse.extract().statusCode(), 201);
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("1. Создание курьера:\n - нельзя создать двух одинаковых курьеров;\n - запрос возвращает правильный код ответа;" +
            "\n - если создать пользователя с логином, который уже есть, возвращается ошибка.")
    public void canNotCreateTwoIdenticalCouriers() {
        courierClient.createCourier(new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()));
        ValidatableResponse createSecondCourier = courierClient.createCourier(new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName()));
        assertEquals(createSecondCourier.extract().statusCode(), 409);
        assertEquals(createSecondCourier.extract().path("message"), "Этот логин уже используется");
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("1. Создание курьера:\n - если одного из полей нет, запрос возвращает ошибку.")
    public void canNotCreateCourierWithoutPassword() {
        Courier courierWithoutPassword = Courier.builder()
                .login(courier.getLogin())
                .firstName(courier.getFirstName())
                .build();
        String errorMessage = courierClient.createFailedCourier(courierWithoutPassword);
        assertEquals("Недостаточно данных для создания учетной записи", errorMessage);
        //Код ответа проверяется внутри самого метода createFailedCourier
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("1. Создание курьера:\n - если одного из полей нет, запрос возвращает ошибку.")
    public void canNotCreateCourierWithoutLogin() {
        Courier courierWithoutLogin = Courier.builder()
                .password(courier.getPassword())
                .firstName(courier.getFirstName())
                .build();
        String errorMessage = courierClient.createFailedCourier(courierWithoutLogin);
        assertEquals("Недостаточно данных для создания учетной записи", errorMessage);
        //Код ответа проверяется внутри самого метода createFailedCourier
    }
}
