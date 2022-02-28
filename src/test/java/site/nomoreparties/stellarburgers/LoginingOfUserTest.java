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

public class LoginingOfUserTest {

    UserMethods userMethods = new UserMethods();
    private String accessToken;
    private String email;
    private String password;


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        SetUser randomUser = CreateRandomUser.createNewRandomUser();
        ValidatableResponse response = UserMethods.createUser(randomUser);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        email = response.extract().path("user.email");
        password = randomUser.getPassword();
        assertEquals("User didn't Login", 200, statusCode);
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


    @DisplayName("Кейс: Логин под существующим пользователем")
    @Description("Авторизация под существующим Логином")
    @Test
    public void userCanLogin() {
        ValidatableResponse response = userMethods.loginUser(new SetUser(email, password));
        int statusCode = response.extract().statusCode();
        assertEquals("Wrong Status Code. Should be - 200 ok", 200, statusCode);
    }


    @DisplayName("Кейс: Логин с неверным логином и паролем.")
    @Description("Авторизация под существующим Логином c неверным паролем")
    @Test
    public void userCantLoginWithIncorrectPassword() {
        ValidatableResponse response = userMethods.loginUser(new SetUser(email, "qwerty1234"));
        int statusCode = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Wrong message.Should be - 401 Unauthorized", 401, statusCode);
        assertEquals("Incorrect message. Message didn't match with expected", "email or password are incorrect", message);
    }
}
