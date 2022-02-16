package ru.yandex.praktikum;
import static io.restassured.RestAssured.given;

import io.qameta.allure.Step;


public class CreateUser {

    private SetUser curentUser;


    @Step("POST https://stellarburgers.nomoreparties.site/api/auth/register")
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

    public void createUserWithToken(){
        curentUser = userRegistration();
    }

    public void printToken(){

        if (curentUser.accessToken == null) {
            System.out.println("Юзер удален, токен отсутствует");
        }
        System.out.println(curentUser.accessToken);
    }

    public void deleteUser(){

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
            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            curentUser = null;


    }

}
