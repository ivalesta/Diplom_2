package ru.practicum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.practicum.basis.Constants;
import ru.practicum.basis.OrderBasis;

import static io.restassured.RestAssured.given;

public class OrderOperations {
    static Constants constants = new Constants();

    @Step("Создание заказа с авторизацией")
    public Response sendPostRequestCreateOrderAuthorized(OrderBasis order, String accessToken) {
        return given().log().all()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(constants.ORDER_CREATE_URL);
    }

    @Step("Создание заказа без авторизации")
    public Response sendPostRequestCreateOrderNotAuthorized(OrderBasis order) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(constants.ORDER_CREATE_URL);
    }
}
