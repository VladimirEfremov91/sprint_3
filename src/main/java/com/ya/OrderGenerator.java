package com.ya;
import io.qameta.allure.Step;

public class OrderGenerator {

    @Step("Гененрация данных заказа для проверки цвета {orderColor}")
    public static Order getOrderForColorTest(String[] orderColor) {

        String orderFirstName = "Вован";
        String orderLastName = "Иванов";
        String orderAddress = "Крсаная площадь, д. 1";
        int orderMetroStation = 15;
        String orderPhone = "88002000600";
        int orderRentTime = 5;
        String orderDeliveryDate = "2022-12-12" ;
        String orderComment = "Это генератор заказа по умолчанию для проверки цвета";

        return new Order(orderFirstName, orderLastName, orderAddress, orderMetroStation, orderPhone, orderRentTime, orderDeliveryDate, orderComment, orderColor);
    }


}
