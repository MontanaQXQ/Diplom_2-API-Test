package ru.yandex.praktikum;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLOutput;

import static org.junit.Assert.*;

public class CreateUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test

    public void makeRandomUser(){

       SetUser user = CreateRandomUser.createNewRandomUser();




    }

    @Test

    public void makeRandomUserWithToken(){
        CreateUser der = new CreateUser();
        der.getCorrectAuth();

    }

}