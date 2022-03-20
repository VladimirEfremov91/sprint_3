package com.ya;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class CourierGenerator {

    @Step("Генерация случайных данных курьера")
    public static Courier getRandom() {
        final String courierLogin = RandomStringUtils.randomAlphabetic(10);
        final String courierPassword = RandomStringUtils.randomAlphabetic(10);
        final String courierFirstName = RandomStringUtils.randomAlphabetic(10);
        Allure.addAttachment("Логин: ", courierLogin);
        Allure.addAttachment("Пароль: ", courierPassword);
        Allure.addAttachment("Имя: ", courierFirstName);
        return new Courier(courierLogin, courierPassword, courierFirstName);
    }
}
