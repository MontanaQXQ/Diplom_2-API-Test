package ru.yandex.praktikum;
import static io.restassured.RestAssured.given;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import java.util.List;
import java.util.Map;
public class OrderMethods {

    //получение данных об ингредиентах
    @Step("Отправка запроса к эндпоинту для получения данных об ингредиентах")
    public ValidatableResponse getIngredients(){
        return given()
                .spec(Base.getBaseSpec())
                .when()
                .get(EndPointsOrder.GET_INGREDIENTS)
                .then();

    }

    //создание заказа
    @Step("Отправка запроса к эндпоинту для создания заказа")
    public ValidatableResponse makeOrder(String accessToken, Map<String, List<String>> userOrder){
        return given()
                .spec(Base.getBaseSpec())
                .header("Authorization", accessToken)
                .and()
                .body(userOrder)
                .log().body()
                .when()
                .post(EndPointsOrder.POST_GET_USER_ORDERS)
                .then()
                .log().body();
    }

    //получение заказов пользователя
    @Step("Отправка запроса к эндпоинту для получения заказов пользователя")
    public ValidatableResponse getOrders(String accessToken){
        return given()
                .spec(Base.getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .get(EndPointsOrder.POST_GET_USER_ORDERS)
                .then();
    }
}
