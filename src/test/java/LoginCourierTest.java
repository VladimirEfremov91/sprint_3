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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@Epic("Работа с курьерами")
@Feature("Логин курьера")
public class LoginCourierTest {

    CourierClient courierClient;
    Courier courier;
    CourierCredentials credentials;
    int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        credentials = new CourierCredentials(courier.getLogin(), courier.getPassword());
        courierClient.createCourier(courier);
    }

    @After
    public void tearDown() {
        courierId = courierClient.loginForGetId(credentials).extract().path("id");
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Авторизация с корректными учетными данными")
    @Description("2. Логин курьера:\n - курьер может авторизоваться;\n - успешный запрос возвращает id.")
    public void courierLoginWithValidCredentials() {
        ValidatableResponse loginResponse = courierClient.login(credentials);
        courierId = loginResponse.extract().path("id");
        assertEquals(loginResponse.extract().statusCode(), 200);
        assertThat("Courier ID is incorrect", courierId, is(not(0)));
    }

    @Test
    @DisplayName("Авторизация с неправильным логином")
    @Description("2. Логин курьера:\n - система вернёт ошибку, если неправильно указать логин или пароль;" +
            "\n - если авторизоваться под несуществующим пользователем, запрос возвращает ошибку.")
    public void courierLoginWithWrongLogin() {
        CourierCredentials credentialsWithWrongLogin = CourierCredentials.builder()
                .login(credentials.getLogin() + "1")
                .password(credentials.getPassword())
                .build();
        ValidatableResponse loginResponse = courierClient.loginFailed(credentialsWithWrongLogin);
        assertEquals(loginResponse.extract().statusCode(), 404);
        assertEquals(loginResponse.extract().path("message"), "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Авторизация с неправильным паролем")
    @Description("2. Логин курьера:\n - система вернёт ошибку, если неправильно указать логин или пароль;" +
            "\n - если авторизоваться под несуществующим пользователем, запрос возвращает ошибку.")
    public void courierLoginWithWrongPassword() {
        CourierCredentials credentialsWithWrongLogin = CourierCredentials.builder()
                .login(credentials.getLogin())
                .password(credentials.getPassword() + "1")
                .build();
        ValidatableResponse loginResponse = courierClient.loginFailed(credentialsWithWrongLogin);
        assertEquals(loginResponse.extract().statusCode(), 404);
        assertEquals(loginResponse.extract().path("message"), "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("2. Логин курьера:\n - если какого-то поля нет, запрос возвращает ошибку.")
    public void courierLoginWithoutLogin() {
        CourierCredentials credentialsWithoutLogin = CourierCredentials.builder()
                .password(credentials.getPassword())
                .build();
        ValidatableResponse loginResponse = courierClient.loginFailed(credentialsWithoutLogin);
        assertEquals(loginResponse.extract().statusCode(), 400);
        assertEquals(loginResponse.extract().path("message"), "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("2. Логин курьера:\n - если какого-то поля нет, запрос возвращает ошибку.")
    public void courierLoginWithoutPassword() {
        CourierCredentials credentialsWithoutPassword = CourierCredentials.builder()
                .login(credentials.getLogin())
                .build();
        ValidatableResponse loginResponse = courierClient.loginFailed(credentialsWithoutPassword);
        assertEquals(loginResponse.extract().statusCode(), 400);
        assertEquals(loginResponse.extract().path("message"), "Недостаточно данных для входа");
    }


}

