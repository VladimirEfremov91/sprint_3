package com.ya;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class OrderClient extends ScooterRestClient {
    private static final String ORDER_PATH = "/api/v1/orders";

    @Step("Получение списка доступных заказов")
    public ValidatableResponse getAvailableOrdersList() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("Отмена заказа {orderId}")
    public static ValidatableResponse cancelOrder(int orderId) {
        String json = "{\"track\": " + orderId + "}";
        return given()
                .spec(getBaseSpec())
                .body(json)
                .when()
                .put(ORDER_PATH + "/cancel")
                .then().log().all();
    }

    @Step("Поиск заказа {orderId}")
    public static ValidatableResponse checkOrderByTrack(int orderId) {
        return given()
                .spec(getBaseSpec())
                .queryParam("t", orderId)
                .when()
                .get(ORDER_PATH + "/track")
                .then().log().all();
    }

}
