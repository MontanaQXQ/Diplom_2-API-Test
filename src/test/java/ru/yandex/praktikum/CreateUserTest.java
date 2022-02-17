package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static io.restassured.RestAssured.given;

public class CreateUserTest {

    UserMethods userMethods = new UserMethods();
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userMethods.deleteUser(accessToken);
        }
    }


    //проверка созданяи рандомных данных
    @DisplayName("проверка созданяи рандомных данных")
    @Description("генерирую данные для  регистрации пользователя")
    @Test
    public void makeRandomUser(){
        SetUser user = CreateRandomUser.createNewRandomUser();
    }

    //Можно создать пользователя
    @DisplayName("Кейс:  уникального пользователя")
    @Description("Регистрирую пользователя")
    @Test
    public void userRegistrationWithToken(){
        ValidatableResponse response = userMethods.createUser(CreateRandomUser.createNewRandomUser());
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        assertEquals(200, statusCode);
    }
    @DisplayName("Кейс: Cоздать пользователя, который уже зарегистрирован")
    @Description("Регистрирую пользователя с данными которые уже зарегестрированы")
    @Test
    public void cantRegistrateSameUser(){
        ValidatableResponse response = userMethods.createUser(new SetUser("tyuiop@yandex.ru","tyuiop","tyuiop"));
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        assertEquals(200, statusCode);
        ValidatableResponse responseTwo = userMethods.createUser(new SetUser("tyuiop@yandex.ru","tyuiop","tyuiop"));
        int statusCodeSecond = responseTwo.extract().statusCode();
        String message = responseTwo.extract().path("message");
        assertEquals(403, statusCodeSecond);
        assertEquals("User already exists", message);
    }

    @DisplayName("Кейс:Cоздать пользователя и не заполнить одно из обязательных полей.")
    @Description("Не указываю Email")
    @Test
    public void registrateUserWithoutOneField(){
        ValidatableResponse response = userMethods.createUser(new SetUser("","tyuiop","tyuiop"));
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals(403, statusCode);
        assertEquals("Email, password and name are required fields", message);

    }




}