package com.geekbrains.spoonacullar.Homework4_RestAssured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.stream.Stream;

import static io.restassured.RestAssured.*;
import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static org.hamcrest.Matchers.*;

public class Homework4_RestAssured {

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "https://api.spoonacular.com";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addParam("apiKey", "b1d0db4af49a401d9593fbbd4414cc96")
                .build();
    }

//    @BeforeEach
//    void beforeTest() {
//        requestSpecification = new RequestSpecBuilder()
//                .addQueryParam("apiKey", "b1d0db4af49a401d9593fbbd4414cc96")
//                .addQueryParam("includeNutrition", "false")
//                .addQueryParam("hash", "e9c97de3d2ec97cb715e1c50640b4c0df4e029d6")
//                .log(LogDetail.ALL)
//                .build();
//    }

    @Test
    @Order(1)
    void test0_AddToShoppingList() {

        String actually = RestAssured.given()
                .param("apiKey", "b1d0db4af49a401d9593fbbd4414cc96")
                .param("username", "your-users-name428")
                .param("hash", "e9c97de3d2ec97cb715e1c50640b4c0df4e029d6")
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .time(lessThanOrEqualTo(1500L))
                .body("item", is("1 package baking powder"))
                .body("aisle", is("Baking"))
                .body("parse", is(true))
                .log()
                .body()
                .when()
                .post("/mealplanner/your-users-name428/shopping-list/items")
                .body()
                .asPrettyString();

        String expected = readResourceAsString("expected.json");

        JsonAssert.assertJsonEquals(
                expected,
                actually,
                JsonAssert.when(IGNORING_ARRAY_ORDER)
        );
    }

//    @ParameterizedTest
//    @MethodSource("resources")
//    void testImageRecognize(String image) {
//        RestAssured.given()
//                .log()
//                .all()
//                .param("imageUrl", image)
//                .expect()
//                .statusCode(200)
//                .body("status", is("success"))
//                .body("category", is("burger"))
//                .body("probability", greaterThan(0.6f))
//                .log()
//                .all()
//                .when()
//                .get("/food/images/classify");
//    }

    public static Stream<Arguments> resources() {
        Arguments f1 = Arguments.of("https://cdn.discordapp.com/icons/525976020919123981/f2ccc3ec3e36988bfa65da0bdae715c8.jpg");
        Arguments f2 = Arguments.of("https://burger-king-kupon.ru/wp-content/uploads/2022/03/1648284144_48dc525c690ab68339a7226c1087654a.png");
        Arguments f3 = Arguments.of("https://bigoven-res.cloudinary.com/image/upload/t_recipe-256/hanger-steak-sandwich-with-bourbon-creamed-spinach-2204420.jpg");
        return Stream.of(f1, f2, f3);
    }

    private String readResourceAsString(String resourceName) {
        // ComplexSearchApiTest/resourceName
        String path = getClass().getSimpleName() + FileSystems.getDefault().getSeparator() + resourceName;
        try (InputStream inputStream = getClass().getResourceAsStream(path)) {
            assert inputStream != null;
            byte[] data = inputStream.readAllBytes();
            return new String(data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
