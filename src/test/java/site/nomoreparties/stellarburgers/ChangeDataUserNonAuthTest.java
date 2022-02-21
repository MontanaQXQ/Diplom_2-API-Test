package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.concurrent.TimeUnit;

@RunWith(Parameterized.class)
public class ChangeDataUserNonAuthTest {

    private UserMethods userMethods = new UserMethods();
    private String accessToken;
    private final SetUser changeUserData;
    private final String testlable;

    public ChangeDataUserNonAuthTest(String testlable,SetUser changeUserData){

        this.changeUserData = changeUserData;
        this.testlable = testlable;

    }

    @Parameterized.Parameters(name = "{0}: {1} = {2}")
    public static Object[][] changeParameter(){
        return new Object[][]{


                {"изменение имени  неавторизованного пользователя",CreateRandomUser.setNewName()
                },

                { "изменение электронной почты неавторизованного пользователя",CreateRandomUser.setNewEmail()
                },
                {"изменение пароля неавторизованного пользователя",CreateRandomUser.setNewPassword()
                }
        };
    }

    @After
    public void tearDown() throws InterruptedException {
        if (accessToken != null) {
            ValidatableResponse deleteResponse  = userMethods.deleteUser(accessToken);
            int statusCode = deleteResponse.extract().statusCode();
            assertEquals(202, statusCode);
            TimeUnit.SECONDS.sleep(5);

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
        assertEquals(200, statusCode);
    }

    @Test
   //Изменение данных пользователя без авторизации
    public void userCanNotChangeNameWithoutLoginTest(){

        ValidatableResponse changeResponse = userMethods.changeUserDataInPersonalAccountWithouAuth(changeUserData);
        int statusCode = changeResponse.extract().statusCode();
        String message = changeResponse.extract().path("message");
        assertEquals(401, statusCode);
        assertEquals("You should be authorised", message);
    }



}