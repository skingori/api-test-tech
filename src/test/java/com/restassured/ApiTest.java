package com.restassured;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

@Order(1)
public class ApiTest {

    private static final String BASE_URL = "https://reqres.in/api";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @AfterAll
    static void tearDown() {
        deleteAllUsers();
    }

    @Test
    public void testCreateUsers() {
        given()
                .contentType("application/json")
                .body("""
                                {
                                    "name": "morpheus",
                                    "job": "leader"
                                }""")
                .when()
                .post("/users")
                .then()
                .statusCode(201);
    }

    @Test
    public void testRetrieveSingleUser() {
        given()
                .when()
                .get("/users/" + getRandomUserIDByKey())
                .then()
                .statusCode(200);

    }

    @Test()
    public void testUpdateAnExistingUser() {
        given()
                .contentType("application/json")
                .body("""
                                {
                                    "name": "morpheus",
                                    "job": "zion resident"
                                }""")
                .when()
                .put("/users/" + getRandomUserIDByKey())
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteAnExistingUser() {
        given()
                .when()
                .delete("/users/" + getRandomUserIDByKey())
                .then()
                .statusCode(204);
    }

    @Test
    public void testRetrieveANonExistingUser() {

        given()
                .when()
                .get("/users/" + getRandomUserIDByKey() + 404)
                .then()
                .statusCode(404);
    }

    @Test
    public void testUpdateANonExistingUser() {
        given()
                .contentType("application/json")
                .body("""
                                {
                                    "name": "morpheus",
                                    "job": "zion resident"
                                }""")
                .when()
                .put("/users/" + getRandomUserIDByKey() + 404)
                .then()
                .statusCode(200);
    }

    public static String getRandomUserIDByKey() {
        Response response = given()
                .when()
                .get("/users");
        List<String> ids = response.path("data.id");
        Random random = new Random();
        int randomIndex = random.nextInt(ids.size());
        return String.valueOf(ids.get(randomIndex));
    }

    public static void deleteAllUsers() {
        Response response = given()
                .when()
                .get("/users");
        List<Integer> ids = response.path("data.id");
        for (Integer id : ids) {
            System.out.println("Deleting user with id: " + id);
            given()
                    .when()
                    .delete("/users/" + id);
        }
    }

}
