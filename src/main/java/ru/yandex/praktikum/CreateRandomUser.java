package ru.yandex.praktikum;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class CreateRandomUser {

    @Step("Генерация данных пользователя (рандомные данные)")
        public static SetUser createNewRandomUser(){
            SetUser setUser = new SetUser();
            setUser.setEmail(RandomStringUtils.randomAlphabetic(5) + "@yandex.ru");
            setUser.setPassword(RandomStringUtils.random(6,true,true));
            setUser.setName(RandomStringUtils.randomAlphabetic(5));
            return setUser;}

    public static SetUser setNewEmail(){
            return new SetUser().setEmail(RandomStringUtils.randomAlphabetic(5) + "@yandex.ru");
        }

    public static SetUser setNewPassword(){
            return new SetUser().setPassword(RandomStringUtils.random(6,true,true));
        }

    public static SetUser setNewName(){
            return new SetUser().setName(RandomStringUtils.randomAlphabetic(5));
        }


}
