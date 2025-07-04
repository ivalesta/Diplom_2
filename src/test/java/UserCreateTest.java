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

public class UserCreateTest {
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
    @DisplayName("Успешное создание уникального пользователя. Код ответа: 200 ОК, тело ответа success: true")
    @Description("Позитивная проверка POST-запроса api/auth/register")
    public void createUserTest() {
        Response response = userCreate.sendPostRequestCreateUser(user);
        accessToken = response.then().extract().path("accessToken").toString();
        response.then().log().all()
                .assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Попытка создания пользователя, который уже зарегистирован. Код ответа 403 Forbidden, тело ответа success: false, message: User already exists")
    @Description("Негативная проверка POST-запроса api/auth/register")
    public void createUserTwiceTest(){
        String name = RandomStringUtils.randomAlphanumeric(4, 10);
        String email = RandomStringUtils.randomAlphanumeric(4, 10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4, 10);

        UserBasis user = new UserBasis(name, email, password);
        Response responseBasis = userCreate.sendPostRequestCreateUser(user);
        userCreate.sendPostRequestCreateUser(user);
        accessToken = responseBasis.then().extract().path("accessToken").toString();

        Response response = userCreate.sendPostRequestCreateUser(user);
        response.then().log().all()
                .assertThat().statusCode(403).and().body( "success", Matchers.is(false)).and().body("message", Matchers.is("User already exists"));
    }

    @Test
    @DisplayName("Попытка создания пользователя, если нет поля name. Код ответа 403, тело ответа success: false, message: Email, password and name are required fields")
    @Description("Негативная проверка POST-запроса api/auth/register")
    public void createUserWithoutNameTest(){
        String email = RandomStringUtils.randomAlphanumeric(4, 10) + "@yandex.ru";
        String password = RandomStringUtils.randomAlphabetic(4, 10);

        UserBasis user = new UserBasis();
        user.setUserEmail(email);
        user.setUserPassword(password);

        Response response = userCreate.sendPostRequestCreateUser(user);
        response.then().log().all()
                .assertThat().statusCode(403).and().body( "success", Matchers.is(false)).and().body("message", Matchers.is("Email, password and name are required fields"));

        if(response.path("accessToken") != null) {
            accessToken = response.then().extract().path("accessToken").toString();
        }
    }

    @Test
    @DisplayName("Попытка создания пользователя, если нет поля email. Код ответа 403, тело ответа success: false, message: Email, password and name are required fields")
    @Description("Негативная проверка POST-запроса api/auth/register")
    public void createUserWithoutEmailTest(){
        String name = RandomStringUtils.randomAlphanumeric(4, 10);
        String password = RandomStringUtils.randomAlphabetic(4, 10);

        UserBasis user = new UserBasis();
        user.setUserName(name);
        user.setUserPassword(password);

        Response response = userCreate.sendPostRequestCreateUser(user);
        response.then().log().all()
                .assertThat().statusCode(403).and().body( "success", Matchers.is(false)).and().body("message", Matchers.is("Email, password and name are required fields"));

        if(response.path("accessToken") != null) {
            accessToken = response.then().extract().path("accessToken").toString();
        }
    }

    @Test
    @DisplayName("Попытка создания пользователя, если нет поля password. Код ответа 403, тело ответа success: false, message: Email, password and name are required fields")
    @Description("Негативная проверка POST-запроса api/auth/register")
    public void createUserWithoutPasswordTest(){
        String name = RandomStringUtils.randomAlphanumeric(4, 10);
        String email = RandomStringUtils.randomAlphanumeric(4, 10) + "@yandex.ru";

        UserBasis user = new UserBasis();
        user.setUserName(name);
        user.setUserEmail(email);

        Response response = userCreate.sendPostRequestCreateUser(user);
        response.then().log().all()
                .assertThat().statusCode(403).and().body( "success", Matchers.is(false)).and().body("message", Matchers.is("Email, password and name are required fields"));

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
