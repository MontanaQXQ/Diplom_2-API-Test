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
import static org.junit.Assert.assertEquals;
import java.util.concurrent.TimeUnit;

public class LoginingOfUserTest {

    UserMethods userMethods = new UserMethods();
    private String accessToken;
    private String email;
    private String password;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        SetUser randomUser = CreateRandomUser.createNewRandomUser();
        ValidatableResponse response = userMethods.createUser(randomUser);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        email = response.extract().path("user.email");
        password = randomUser.getPassword();
        assertEquals(200, statusCode);

    }

    @After
    public void tearDown() throws InterruptedException {
        if (accessToken != null) {
            ValidatableResponse deleteResponse  = userMethods.deleteUser(accessToken);
            int statusCode = deleteResponse.extract().statusCode();
            assertEquals(202, statusCode);
            TimeUnit.SECONDS.sleep(5);

        }
    }



    @DisplayName("Кейс: Kогин под существующим пользователем")
    @Description("Авторизация под существующим Логином")
    @Test
    public void userCanLogin(){
        ValidatableResponse response = userMethods.loginUser(new SetUser(email,password));
        int statusCode = response.extract().statusCode();
        assertEquals(200, statusCode);

    }


    @DisplayName("Кейс: Логин с неверным логином и паролем.")
    @Description("Авторизация под существующим Логином c неверным паролем")
    @Test
    public void userCantLoginWithIncorrectPassword(){
        ValidatableResponse response = userMethods.loginUser(new SetUser(email,"qwerty1234"));
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("email or password are incorrect", message);

    }
}
