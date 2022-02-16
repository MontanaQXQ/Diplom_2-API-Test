package ru.yandex.praktikum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CreateUserTest {

    UserMethods userMethods = new UserMethods();

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

    }

       @After
   public void tearDown() {
        userMethods.deleteUser();
    }

    //проверка созданяи рандомных данных
    @DisplayName("проверка созданяи рандомных данных")
    @Description("генерирую данные для  регистрации пользователя")
    @Test
    public void makeRandomUser(){
       SetUser user = CreateRandomUser.createNewRandomUser();
    }

    //Можно создать пользователя
    @DisplayName("Кейс: Создать уникального пользователя;")
    @Description("Регистрирую пользователя")
    @Test
    public void userRegistrationWithToken(){
        userMethods.createUserWithToken();
    }

    @DisplayName("Кейс: Создать уникального пользователя;")
    @Description("Регистрирую пользователя")
    @Test
    public void cantRegistrateSameUser(){
        userMethods.getAccessToken(new SetUser("montyponty@yandex.ru","1234qwer","montyponty"));
        userMethods.getAccessToken(new SetUser("montyponty@yandex.ru","1234qwer","montyponty"));

    }




}