

import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqresInTests {

    @Test
    void getListUsersTest() {
        given()

                .when()
                .get("https://reqres.in/api/users?page=2")

                .then()
                .log().body()
                .log().status()
                .body("page", is(2))
                .body("data.id", hasItems(7, 12))
                .statusCode(200);
    }

    @Test
    void getSingleUserTest() {
        String supportText = "To keep ReqRes free, contributions towards server costs are appreciated!";
        given()

                .when()
                .get("https://reqres.in/api/users/2")

                .then()
                .log().body()
                .log().status()
                .body("data.id", is(2))
                .body("data.first_name", is("Janet"))
                .body("support.text", is(supportText))
                .statusCode(200);
    }

    @Test
    void postCreateTest() {
        given()
                .body("{\"name\": \"morpheus\", \"job\": \"leader\"\n}")
                .contentType(ContentType.JSON)

                .when()
                .post("https://reqres.in/api/users")

                .then()
                .log().body()
                .log().status()
                .statusCode(201);
    }
    @Test
    void postRegisterSuccessfulTest() {
        given()
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"pistol\"\n" +
                        "}")
                .contentType(ContentType.JSON)

                .when()
                .post("https://reqres.in/api/register")

                .then()
                .log().body()
                .log().status()
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"))
                .statusCode(200);
    }
    @Test
    void getDelayedResponseTest() {

        given()
                .when()
                .contentType(ContentType.JSON)
                .get("https://reqres.in/api/users?delay=3")

                .then()
                .log().body()
                .log().status()
                .statusCode(200);
    }
}
