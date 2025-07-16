import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.LoginOperations;
import ru.practicum.OrderOperations;
import ru.practicum.basis.Constants;
import ru.practicum.basis.OrderBasis;
import ru.practicum.basis.UserBasis;

import java.util.ArrayList;
import java.util.List;

public class OrderCreateTest {
    LoginOperations userCreate;
    OrderOperations orderCreate;
    UserBasis user;
    OrderBasis order;
    Constants constants = new Constants();
    String accessToken;
    List<String> tastyBurger;

    @Before
    public void setUp() {
        RestAssured.baseURI = constants.BASE_URL;

        userCreate = new LoginOperations();
        String name = RandomStringUtils.randomAlphanumeric(4, 10);
        String email = RandomStringUtils.randomAlphanumeric(4, 10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4, 10);

        user = new UserBasis(name, email, password);
        Response response = userCreate.sendPostRequestCreateUser(user);

        if(response.path("accessToken") != null) {
            accessToken = response.then().extract().path("accessToken").toString();
        }

        orderCreate = new OrderOperations();

        tastyBurger = new ArrayList<>();
        tastyBurger.add(constants.ITEM_BUN_R2D3);
        tastyBurger.add(constants.ITEM_MAIN_PROTOSTOMIA);
        tastyBurger.add(constants.ITEM_SAUCE_SPICY);

        order = new OrderBasis(tastyBurger);

    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами. Код ответа 200 OK. Тело ответа success: true")
    @Description("Позитивная проверка POST-запроса api/orders")
    public void createOrderAuthorizedTest() {
        Response response = orderCreate.sendPostRequestCreateOrderAuthorized(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами, без авторизации. Код ответа 200 OK. Тело ответа success: true")
    @Description("Позитивная проверка POST-запроса api/orders")
    public void createOrderWithoutAuthorizedTest() {
        Response response = orderCreate.sendPostRequestCreateOrderNotAuthorized(order);
        response.then().log().all()
                .assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, без авторизации. Код ответа 400 Bad Request. Тело ответа success: false, message: Ingredient ids must be provided")
    @Description("Негативная проверка POST-запроса api/orders")
    public void createOrderWithoutAuthorizedAndIngredientTest() {
        List<String>tastyEmptyBurger = new ArrayList<>();
        OrderBasis emptyOrder = new OrderBasis(tastyEmptyBurger);

        Response response = orderCreate.sendPostRequestCreateOrderNotAuthorized(emptyOrder);
        response.then().log().all()
                .assertThat().statusCode(400).and().body("success", Matchers.is(false)).and().body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без авторизации, с несуществующим хешем ингредиента. Код ответа 500 Internal Server Error")
    @Description("Негативная проверка POST-запроса api/orders")
    public void createOrderWithoutAuthorizedAndWrongIngredientTest() {
        List<String>tastyWrongBurger = new ArrayList<>();
        tastyWrongBurger.add("very wrong hash");
        OrderBasis emptyOrder = new OrderBasis(tastyWrongBurger);

        Response response = orderCreate.sendPostRequestCreateOrderNotAuthorized(emptyOrder);
        response.then().log().all()
                .assertThat().statusCode(500);
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userCreate.sendDeleteRequestUser(accessToken);
        }
    }
}
