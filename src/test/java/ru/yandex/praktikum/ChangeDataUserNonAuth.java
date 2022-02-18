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
import org.junit.Test;




public class ChangeDataUserNonAuth {

    UserMethods userMethods = new UserMethods();
    private String accessToken;
    private String email;
    private String password;
    private String name;
    private CreateRandomUser randomUser;

    @After
    public void tearDown() {
        if (accessToken != null) {
            ValidatableResponse deleteResponse  = userMethods.deleteUser(accessToken);
            int statusCode = deleteResponse.extract().statusCode();
            assertEquals(202, statusCode);

        }
    }

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
        name = randomUser.getName();
        assertEquals(200, statusCode);

    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    public void userCanNotChangeDataWithoutLoginTest(){
////// ИСПРАВИТЬ!!!!!! ПОКА ЧТО НЕ РАБОТАЕТ КАК НУЖНО!
        ValidatableResponse changeResponse = userMethods.changeUserDataInPersonalAccountWithouAuth(new SetUser(CreateRandomUser.setNewEmail().getEmail(),CreateRandomUser.setNewName().getName()));
        int statusCode = changeResponse.extract().statusCode();
        String message = changeResponse.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("You should be authorised", message);
    }



}