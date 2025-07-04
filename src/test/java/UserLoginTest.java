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
import ru.practicum.basis.Constants;
import ru.practicum.basis.UserBasis;

public class UserLoginTest {
    LoginOperations userCreate ;
    UserBasis user;
    Constants constants = new Constants();
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = constants.BASE_URL;

        userCreate = new LoginOperations();
        String name = RandomStringUtils.randomAlphanumeric(4, 10);
        String email = RandomStringUtils.randomAlphanumeric(4, 10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4, 10);

        user = new UserBasis(name, email, password);
    }

    @Test
    @DisplayName("Успешный логин существующего пользователя. Тело ответа success: true, код ответа 200 ОК")
    @Description("Позитивная проверка POST-запроса api/auth/login")
    public void loginUserTest(){
        userCreate.sendPostRequestCreateUser(user);

        Response response = userCreate.sendPostRequestLoginUser(user);
        response.then().log().all()
                .assertThat().statusCode(200).and().body( "success", Matchers.is(true));

        if(response.path("accessToken") != null) {
            accessToken = response.then().extract().path("accessToken").toString();
        }
    }

    @Test
    @DisplayName("Попытка логина пользователя, если пара логин-пароль несуществующая. Тело ответа success: false, message: email or password are incorrect, код ответа 401 Unauthorized.")
    @Description("Негативная проверка POST-запроса api/auth/login")
    public void loginUserWithWrongPairTest(){
        String name = RandomStringUtils.randomAlphanumeric(4, 10);
        String email = RandomStringUtils.randomAlphanumeric(4, 10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4, 10);
        UserBasis user = new UserBasis(name, email, password);

        userCreate.sendPostRequestCreateUser(user);

        email = "AbsolutelyWrongEmail.@yandex.ru";
        password = "AbsolutelyWrongPassword.@yandex.ru";

        UserBasis userSad = new UserBasis();
        userSad.setUserEmail(email);
        userSad.setUserPassword(password);

        Response response = userCreate.sendPostRequestLoginUser(userSad);
        response.then().log().all()
                .assertThat().statusCode(401).and().body( "success", Matchers.is(false)).and().body( "message", Matchers.is("email or password are incorrect"));

        if(response.path("accessToken") != null) {
            accessToken = response.then().extract().path("accessToken").toString();
        }
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userCreate.sendDeleteRequestUser(accessToken);
        }
    }
}
