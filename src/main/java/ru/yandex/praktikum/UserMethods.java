package ru.yandex.praktikum;
import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class UserMethods {

    @Step("Регистрация пользователья с рандомными данными")
    public static ValidatableResponse createUser(SetUser Setuser){
        return given()
                .spec(Base.getBaseSpec())
                .and()
                .body(Setuser)
                .when()
                .post(EndPointsUser.POST_CREATE_USER)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .spec(Base.getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("auth/user")
                .then();

    }

}
