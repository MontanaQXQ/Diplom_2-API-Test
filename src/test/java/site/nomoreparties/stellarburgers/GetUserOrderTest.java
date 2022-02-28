package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class GetUserOrderTest {
    UserMethods userMethods = new UserMethods();
    CreatingOrder creatingOrder = new CreatingOrder();
    OrderMethods orderMethods = new OrderMethods();
    private String accessToken;
    private String accessTokenLogin;
    private String email;
    private String password;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        SetUser randomUser = CreateRandomUser.createNewRandomUser();
        ValidatableResponse response = UserMethods.createUser(randomUser);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        email = response.extract().path("user.email");
        password = randomUser.getPassword();
        assertEquals("User didn't Login", 200, statusCode);


    }

    @After
    public void tearDown() throws InterruptedException {
        if (accessToken != null) {
            ValidatableResponse deleteResponse = userMethods.deleteUser(accessToken);
            int statusCode = deleteResponse.extract().statusCode();
            assertEquals("User Didn't Delete", 202, statusCode);
            TimeUnit.SECONDS.sleep(5);

        }
    }

    @Test
    @DisplayName("Кейс:Получение заказов конкретного пользователя - авторизованный пользователь.")
    public void getOrdersAuthUserTest() {
        ValidatableResponse responseLogin = userMethods.loginUser(new SetUser(email, password));
        accessTokenLogin = responseLogin.extract().path("accessToken");

        List<String> burger = creatingOrder.makeBurger();
        Map<String, List<String>> burgerOrder = new HashMap<>();
        burgerOrder.put("ingredients", burger);
        ValidatableResponse order = orderMethods.makeOrder(accessTokenLogin, burgerOrder);

        ValidatableResponse userOrders = orderMethods.getOrders(accessTokenLogin);
        int statusCodeGetOrder = userOrders.extract().statusCode();
        int orderNumber = order.extract().path("order.number");
        assertEquals("Wrong Status Code. Should be - 200 ok", 200, statusCodeGetOrder);
        Assert.assertNotNull("Order number Cant be Null!", orderNumber);
    }

    @Test
    @DisplayName("Кейс:Получение заказов конкретного пользователя - неавторизованный пользователь.")
    public void getOrdersNonAuthUserTest() {
        ValidatableResponse userOrders = orderMethods.getOrders("");
        int statusCodeGetOrder = userOrders.extract().statusCode();
        String message = userOrders.extract().path("message");
        assertEquals("Wrong status code.Should be - 401 Unauthorized", 401, statusCodeGetOrder);
        assertEquals("Wrong message.Message didn't match with expected", "You should be authorised", message);
    }
}
