package com.restassured;

import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

@Order(1)
public class ApiTests {

    private static final String BASE_URL = "https://reqres.in/api";

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    @Test
    public void createUsers() {
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
    public void retrieveSingleUser() {
        given()
                .when()
                .get("/users/" + getRandomUserIDByKey())
                .then()
                .statusCode(200);

    }

    @Test()
    public void updateAnExistingUser() {
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
    public void deleteAnExistingUser() {
        given()
                .when()
                .delete("/users/" + getRandomUserIDByKey())
                .then()
                .statusCode(204);
    }

    @Test
    public void retrieveANonExistingUser() {

        given()
                .when()
                .get("/users/" + getRandomUserIDByKey() + 404)
                .then()
                .statusCode(404);
    }

    @Test
    public void updateANonExistingUser() {
        given()
                .contentType("application/json")
                .body("{\n"
                        + "    \"name\": \"morpheus\",\n"
                        + "    \"job\": \"zion resident\"\n"
                        + "}")
                .when()
                .put("/users/" + getRandomUserIDByKey() + 404)
                .then()
                .statusCode(200);
    }


    public String getRandomUserIDByKey() {
        Response response = given()
                .when()
                .get("/users");
        List<String> ids = response.path("data.id");
        Random random = new Random();
        int randomIndex = random.nextInt(ids.size());
        return String.valueOf(ids.get(randomIndex));
    }

}
