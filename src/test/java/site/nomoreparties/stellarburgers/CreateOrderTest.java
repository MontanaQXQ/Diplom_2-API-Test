package site.nomoreparties.stellarburgers;


import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest {

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
    @DisplayName("Кейс:Создание заказа с авторизацией и ингредиентами")
    public void userAuthdMakeOrderTest() {
        ValidatableResponse responseLogin = userMethods.loginUser(new SetUser(email, password));
        accessTokenLogin = responseLogin.extract().path("accessToken");

        List<String> burger = creatingOrder.makeBurger();
        Map<String, List<String>> burgerOrder = new HashMap<>();
        burgerOrder.put("ingredients", burger);
        ValidatableResponse order = orderMethods.makeOrder(accessTokenLogin, burgerOrder);
        int statusCode = order.extract().statusCode();
        assertEquals("Wrong Status Code. Should be - 200 ok", 200, statusCode);
    }


    @Test
    @DisplayName("Кейс:Создание заказа без авторизации")
    public void userNonAuthorizedMakeOrderTest() {
        List<String> burger = creatingOrder.makeBurger();
        Map<String, List<String>> burgerOrder = new HashMap<>();
        burgerOrder.put("ingredients", burger);
        System.out.println("========================================================================================================================================");
        ValidatableResponse order = orderMethods.makeOrder("", burgerOrder);
        int statusCode = order.extract().statusCode();
        assertEquals("Wrong Status Code. Should be - 200 ok", 200, statusCode);
    }

    @Test
    @DisplayName("Кейс:Создание заказа без ингредиентов")
    public void userMakeOrderWithoutIngridientTest() {
        ValidatableResponse responseLogin = userMethods.loginUser(new SetUser(email, password));
        accessTokenLogin = responseLogin.extract().path("accessToken");

        Map<String, List<String>> burgerOrder = new HashMap<>();
        ValidatableResponse order = orderMethods.makeOrder(accessTokenLogin, burgerOrder);
        int statusCode = order.extract().statusCode();
        String message = order.extract().path("message");
        assertEquals("Bad Request", 400, statusCode);
        assertEquals("Ingredient ids must be provided", message);
    }

    @Test
    @DisplayName("Кейс:Создание с неверным хешем ингредиентов")
    public void userCanNotMakeOrderWithIncorrectHashOfIngredientsTest() {
        ValidatableResponse responseLogin = userMethods.loginUser(new SetUser(email, password));
        accessTokenLogin = responseLogin.extract().path("accessToken");

        List<String> burger = creatingOrder.makeBurgerWithIncorrectIngredients();
        Map<String, List<String>> burgerOrder = new HashMap<>();
        burgerOrder.put("ingredients", burger);
        ValidatableResponse order = orderMethods.makeOrder(accessTokenLogin, burgerOrder);
        int statusCode = order.extract().statusCode();
        assertEquals("Wrong Status Code. Should be - 500", 500, statusCode);
    }
}
