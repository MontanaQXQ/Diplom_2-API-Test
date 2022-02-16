package ru.yandex.praktikum;
import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.sql.SQLOutput;


public class UserMethods {

    private SetUser curentUser;


    @Step("Гененирация пользователя, с получением его accessToken.")
    public SetUser userRegistration() {
        SetUser newUser =  CreateRandomUser.createNewRandomUser();
        String myEmail = newUser.email;
        String myPassword = newUser.password;
        String myName = newUser.name;
        SetUser auth = new SetUser(myEmail, myPassword,myName);
        getAccessToken(auth);
        System.out.println("====================================");
        System.out.println(auth.accessToken);
        System.out.println("====================================");
        return auth;
    }


    @Step("Метод получения accessToken Пользователя")
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

    @Step("Check statusCode create couriers OK")
    public void checkStatusCode(Response response){
        response.then().assertThat().statusCode(403);
    }
    @Step("Метод регистрации Пользователя")
    public void createUserWithToken(){
        curentUser = userRegistration();

    }



    @Step("Метод метод удаления созданного Пользователя")
    public void deleteUser() {

        if (curentUser.accessToken == null) {
            return;
        }

            given()
                    .spec(Base.getBaseSpec())
                    .auth().oauth2(curentUser.accessToken)
                    .when()
                    .delete("auth/user")
                    .then()
                    .statusCode(202);
            curentUser = null;

    }

//    public Response notRandomUser(){
//
//    }
}
