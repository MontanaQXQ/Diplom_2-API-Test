package ru.yandex.praktikum;
import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserMethods {

    @Step("Запрос для Регистрации пользователья с рандомными данными")
    public static ValidatableResponse createUser(SetUser setUser){
        return given()
                .spec(Base.getBaseSpec())
                .and()
                .body(setUser)
                .when()
                .post(EndPointsUser.POST_CREATE_USER)
                .then();
    }


    @Step("Запрос  для авторизации пользователя")
    public ValidatableResponse loginUser(SetUser setUser){
        return given()
                .spec(Base.getBaseSpec())
                .and()
                .body(setUser)
                .when()
                .post(EndPointsUser.POST_AUTH_USER)
                .then();

    }

    @Step("Запрос  для изменения данных пользователя с авторизацией")
    public ValidatableResponse changeUserDataInPersonalAccount(String accessToken, SetUser setUser){
        return given()
                .spec(Base.getBaseSpec())
                .header("Authorization", accessToken)
                .and()
                .body(setUser)
                .when()
                .patch(EndPointsUser.POST_CHANGE_DATA_USER)
                .then();

    }


    @Step("Запрос  для изменения данных пользователя без  авторизации")
    public ValidatableResponse changeUserDataInPersonalAccountWithouAuth(SetUser SetUser){
        return given()
                .spec(Base.getBaseSpec())
                .and()
                .body(SetUser)
                .when()
                .patch(EndPointsUser.POST_CHANGE_DATA_USER)
                .then();
    }

    @Step("Запрос на Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .spec(Base.getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("auth/user")
                .then();

    }

}
