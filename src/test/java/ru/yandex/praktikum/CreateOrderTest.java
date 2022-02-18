package ru.yandex.praktikum;

import io.qameta.allure.Description;
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
    public void setUp(){
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        SetUser randomUser = CreateRandomUser.createNewRandomUser();
        ValidatableResponse response = userMethods.createUser(randomUser);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        email = response.extract().path("user.email");
        password = randomUser.getPassword();
        assertEquals(200, statusCode);
        ValidatableResponse responseLogin = userMethods.loginUser(new SetUser(email,password));
        int statusCodeLogin = responseLogin.extract().statusCode();
        accessTokenLogin = responseLogin.extract().path("accessToken");
        assertEquals(200, statusCodeLogin);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            ValidatableResponse deleteResponse  = userMethods.deleteUser(accessToken);
            int statusCode = deleteResponse.extract().statusCode();
            assertEquals(202, statusCode);

        }
    }


    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    public void userAuthdMakeOrderTest(){
        List<String> burger = creatingOrder.makeBurger();
        Map<String, List<String>> burgerOrder = new HashMap<>();
        burgerOrder.put("ingredients", burger);
        ValidatableResponse order = orderMethods.makeOrder(accessTokenLogin, burgerOrder);
        int statusCode = order.extract().statusCode();
        assertEquals( 200, statusCode);

    }


}
