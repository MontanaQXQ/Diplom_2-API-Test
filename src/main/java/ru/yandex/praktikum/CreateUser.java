package ru.yandex.praktikum;
import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import java.util.ArrayList;


public class CreateUser {






    public SetUser getCorrectAuth() {
        SetUser newUser =  CreateRandomUser.createNewRandomUser();
        String myEmail = newUser.email;
        String myPassword = newUser.password;
        String myName = newUser.name;
        SetUser auth = new SetUser(myEmail, myPassword,myName);
        getAccessToken(auth);
        System.out.println("------------------------------------");
        System.out.println(auth.accessToken.substring(7));
        System.out.println("------------------------------------");
        return auth;
    }


    @Step("POST https://stellarburgers.nomoreparties.site/api/auth/register")
    public void getAccessToken(SetUser setUser){
        String accessToken =  given()
                .header("Content-type", "application/json")
                .and()
                .body(setUser)
                .when()
                .post(EndPointsUser.POST_CREATE_USER)
                .then().assertThat().statusCode(200)
                .and().extract().body().path("accessToken");
        setUser.accessToken = accessToken.substring(7);

    }

/*

    public void deleteUser(){

            if (getCorrectAuth() == null) {
                return;
            }
            given()
                    .spec(Base.getBaseSpec())
                    .auth().oauth2(Tokens.getAccessToken())
                    .when()
                    .delete("auth/user")
                    .then()
                    .statusCode(202);

    }

*/
}
