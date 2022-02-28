package site.nomoreparties.stellarburgers;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ChangeDataAuthUserStatusCodeOkTest {

    private final SetUser changeUserData;
    private final String testlable;
    private final UserMethods userMethods = new UserMethods();
    private String accessToken;
    private String accessTokenLogin;
    private String email;
    private String password;

    public ChangeDataAuthUserStatusCodeOkTest(String testlable, SetUser changeUserData) {

        this.changeUserData = changeUserData;
        this.testlable = testlable;

    }

    @Parameterized.Parameters(name = "{0}: {1} = {2}")
    public static Object[][] changeParameter() {
        return new Object[][]{

                {"Изменение только пароля у авторизованного пользователя", CreateRandomUser.setNewPassword()
                },

                {"Изменение только имени у  авторизованного пользователя", CreateRandomUser.setNewName()
                },

                {"Изменение только адреса электронной почты у авторизованного пользователя", CreateRandomUser.setNewEmail()
                }
        };
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        SetUser randomUser = CreateRandomUser.createNewRandomUser();
        ValidatableResponse response = UserMethods.createUser(randomUser);
        accessToken = response.extract().path("accessToken");
        email = response.extract().path("user.email");
        password = randomUser.getPassword();

        ValidatableResponse responseLogin = userMethods.loginUser(new SetUser(email, password));
        int statusCodeLogin = responseLogin.extract().statusCode();
        accessTokenLogin = responseLogin.extract().path("accessToken");
        assertEquals("User didn't Login",200, statusCodeLogin);

    }

    @After
    public void tearDown() throws InterruptedException {
        if (accessToken != null) {
            ValidatableResponse deleteResponse = userMethods.deleteUser(accessToken);
            int statusCode = deleteResponse.extract().statusCode();
            assertEquals("User Didn't Delete",202, statusCode);
            TimeUnit.SECONDS.sleep(5);

        }
    }

    @Test
    //Изменение данных авторизованного пользователя с ожидаемым ответом 200 ок
    public void userCanChangeEmailWithAnotherDataAfterLoginTest() {
        ValidatableResponse changeResponse = userMethods.changeUserDataInPersonalAccount(accessTokenLogin, changeUserData);
        int statusCode = changeResponse.extract().statusCode();
        assertEquals("Incorrect Status Code - Should be 200 ok",200, statusCode);
    }
}
