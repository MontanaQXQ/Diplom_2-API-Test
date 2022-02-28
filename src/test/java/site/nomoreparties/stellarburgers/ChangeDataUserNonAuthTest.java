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
public class ChangeDataUserNonAuthTest {

    private final SetUser changeUserData;
    private final String testlable;
    private final UserMethods userMethods = new UserMethods();
    private String accessToken;

    public ChangeDataUserNonAuthTest(String testlable, SetUser changeUserData) {

        this.changeUserData = changeUserData;
        this.testlable = testlable;

    }

    @Parameterized.Parameters(name = "{0}: {1} = {2}")
    public static Object[][] changeParameter() {
        return new Object[][]{

                {"изменение имени  неавторизованного пользователя", CreateRandomUser.setNewName()
                },

                {"изменение электронной почты неавторизованного пользователя", CreateRandomUser.setNewEmail()
                },

                {"изменение пароля неавторизованного пользователя", CreateRandomUser.setNewPassword()
                }
        };
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

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        SetUser randomUser = CreateRandomUser.createNewRandomUser();
        ValidatableResponse response = UserMethods.createUser(randomUser);
        accessToken = response.extract().path("accessToken");
        int statusCode = response.extract().statusCode();
        assertEquals("User didn't Login", 200, statusCode);
    }

    @Test
    //Изменение данных пользователя без авторизации
    public void userCanNotChangeNameWithoutLoginTest() {
        ValidatableResponse changeResponse = userMethods.changeUserDataInPersonalAccountWithouAuth(changeUserData);
        int statusCode = changeResponse.extract().statusCode();
        String message = changeResponse.extract().path("message");
        assertEquals("Wrong status code. Should be - 401", 401, statusCode);
        assertEquals("Wrong message.Should be  ==You should be authorised==", "You should be authorised", message);
    }
}