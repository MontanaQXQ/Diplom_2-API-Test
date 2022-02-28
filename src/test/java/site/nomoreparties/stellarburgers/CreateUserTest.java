package site.nomoreparties.stellarburgers;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


public class CreateUserTest {

    UserMethods userMethods = new UserMethods();
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
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

    //Можно создать пользователя
    @DisplayName("Кейс: Регистрирую  уникального пользователя")
    @Description("Регистрирую пользователя")
    @Test
    public void userRegistrationWithToken() {
        ValidatableResponse response = UserMethods.createUser(CreateRandomUser.createNewRandomUser());
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        assertEquals("Wrong Status Code. Should be - 200 ok", 200, statusCode);
    }

    @DisplayName("Кейс: Cоздать пользователя, который уже зарегистрирован")
    @Description("Регистрирую пользователя с данными которые уже зарегестрированы")
    @Test
    public void cantRegistrateSameUser() {
        ValidatableResponse response = UserMethods.createUser(new SetUser("tyuiop@yandex.ru", "tyuiop", "tyuiop"));
        accessToken = response.extract().path("accessToken");

        ValidatableResponse responseTwo = UserMethods.createUser(new SetUser("tyuiop@yandex.ru", "tyuiop", "tyuiop"));
        int statusCodeSecond = responseTwo.extract().statusCode();
        String message = responseTwo.extract().path("message");
        assertEquals("Wrong Status Code. Should be - 403 Forbidden", 403, statusCodeSecond);
        assertEquals("Wrong Message - not expected message", "User already exists", message);
    }

    @DisplayName("Кейс:Cоздать пользователя и не заполнить одно из обязательных полей.")
    @Description("Не указываю Email")
    @Test
    public void registrateUserWithoutOneField() {
        ValidatableResponse response = UserMethods.createUser(new SetUser("", "tyuiop", "tyuiop"));
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Wrong Status Code. Should be - 403 Forbidden", 403, statusCode);
        assertEquals("Wrong Message - not expected message", "Email, password and name are required fields", message);
    }
}