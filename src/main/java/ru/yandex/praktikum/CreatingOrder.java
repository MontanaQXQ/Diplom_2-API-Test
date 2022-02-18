package ru.yandex.praktikum;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import java.util.ArrayList;
import java.util.List;

public class CreatingOrder {
    private List<String> burger;

    @Step("Создание бургера из ингридиентов")
    public List<String> makeBurger() {

        OrderMethods orderMethods = new OrderMethods();
        ValidatableResponse ingredients = orderMethods.getIngredients();

        List<String> buns = ingredients.extract().jsonPath().getList("data.findAll{it.type =='bun'}._id");
        List<String> fillings = ingredients.extract().jsonPath().getList("data.findAll{it.type =='main'}._id");
        List<String> sauces = ingredients.extract().jsonPath().getList("data.findAll{it.type =='sauce'}._id");


        burger = new ArrayList<>();
        burger.add(buns.get(RandomUtils.nextInt(0, buns.size()-1)));
        burger.add(fillings.get(RandomUtils.nextInt(0, fillings.size()-1)));
        burger.add(sauces.get(RandomUtils.nextInt(0, sauces.size()-1)));
        return burger;
    }

    public String incorrectIngredient(){
        return "61c0c5a71d1f82001bdqwer9q" + RandomStringUtils.randomAlphanumeric(2);
    }

    @Step("Создание бургера из несуществующих ингредиентов")
    public List<String> makeBurgerWithIncorrectIngredients() {
        burger = new ArrayList<>();
        burger.add(incorrectIngredient());
        burger.add(incorrectIngredient());
        return burger;
    }
}
