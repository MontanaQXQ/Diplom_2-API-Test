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
import io.qameta.allure.Description;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.assertEquals;

//@RunWith(Parameterized.class)
public class ChangeDataOfUserAuth {

    UserMethods userMethods = new UserMethods();
    //private final SetUser changeUserData;
    private String accessToken;
    private String accessTokenLogin;
    private String email;
    private String password;
    private String name;
    private SetUser setUser;

/// НУЖНО ДОДЕЛАТЬ ,ПОКА НЕ РАБОТАЕТ!

//    public ChangeDataOfUser(SetUser changeUserData){
//
//        this.changeUserData = changeUserData;
//
//    }
//
//    @Parameterized.Parameters
//    public static Object[][] changeParameter(){
//        return new Object[][]{
//                //изменение адреса электронной почты авторизованного пользователя")
//                {CreateRandomUser.setNewName(),
//                },
//                //изменение пароля авторизованного пользователя
//                {CreateRandomUser.setNewPassword(),
//                },
//                //изменение имени авторизованного пользователя
//                {CreateRandomUser.setNewEmail()
//                }
//        };
//    }

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
/// НУЖНО ДОДЕЛАТЬ, ПОКА НЕ РАБОТАЕТ!
    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    public void userCanChangeEmailAfterLoginTest(){
        SetUser ff = CreateRandomUser.setNewName(); // ИЗМЕНИТЬ ТЕСТОВУЮ ПЕРЕМЕННУ ff
        ValidatableResponse changeResponse = userMethods.changeUserDataInPersonalAccount(accessTokenLogin, ff);// ИЗМЕНИТЬ ТЕСТОВУЮ ПЕРЕМЕННУ ff
        int statusCode = changeResponse.extract().statusCode();
        assertEquals(200, statusCode);
    }
}
