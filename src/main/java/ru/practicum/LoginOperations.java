package ru.practicum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.practicum.basis.Constants;
import ru.practicum.basis.UserBasis;

import static io.restassured.RestAssured.given;

public class LoginOperations {
    static Constants constants = new Constants();

    @Step("Создание пользователя")
    public Response sendPostRequestCreateUser(UserBasis user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(constants.USER_CREATE_URL);
    }

    @Step("Логин пользователя")
    public Response sendPostRequestLoginUser(UserBasis user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(constants.USER_LOGIN_URL);
    }


    @Step("Удаление пользователя")
    public static Response sendDeleteRequestUser(String accessToken){
        return given().log().all()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .delete(constants.USER_DELETE_URL);
    }
}
