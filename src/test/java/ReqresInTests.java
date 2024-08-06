

import io.restassured.http.ContentType;
import models.CreateUserBodyModel;
import models.CreateUserResponseModel;
import models.RegisterUserBodyModel;
import models.RegisterUserResponseModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static specs.CreateUserSpec.createUserRequestSpec;
import static specs.CreateUserSpec.createUserResponseSpec;
import static specs.RegisterUserSpec.registerUserRequestSpec;
import static specs.RegisterUserSpec.registerUserResponseSpec;

public class ReqresInTests extends TestBase {

    @Test
    void getListUsersTest() {
        given()

                .when()
                .get("/api/users?page=2")

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
                .get("/api/users/2")

                .then()
                .log().body()
                .log().status()
                .body("data.id", is(2))
                .body("data.first_name", is("Janet"))
                .body("support.text", is(supportText))
                .statusCode(200);
    }

    @Test
    @Tags({@Tag("WithLombok"), @Tag("CreateUser")})
    void postCreateTest() {
        CreateUserBodyModel createUserBodyModel = new CreateUserBodyModel();
        createUserBodyModel.setName("morpheus");
        createUserBodyModel.setJob("leader");

        CreateUserResponseModel createUserResponseModel = step("Отправление запроса на создание пользователя", () ->
                given(createUserRequestSpec)
                        .body(createUserBodyModel)

                        .when()
                        .post()

                        .then()
                        .spec(createUserResponseSpec)
                        .extract().as(CreateUserResponseModel.class)
        );
        step("Проверка присвоения ID пользователю", () ->
                Assertions.assertNotNull(createUserResponseModel.getId()));

    }

    @Test
    @Tags({@Tag("WithLombok"), @Tag("RegisterUser")})
    void postRegisterSuccessfulTest() {
        RegisterUserBodyModel registerUserBodyModel = new RegisterUserBodyModel();
        registerUserBodyModel.setEmail("eve.holt@reqres.in");
        registerUserBodyModel.setPassword("pistol");
        RegisterUserResponseModel registerUserResponseModel = step("Отправление запроса на регистрацию пользователя", () ->
                given(registerUserRequestSpec)
                        .body(registerUserBodyModel)

                        .when()
                        .post()

                        .then()
                        .spec(registerUserResponseSpec)
                        .extract().as(RegisterUserResponseModel.class));
        step("Проверка полученного ID и токена пользователя", () -> {
            Assertions.assertEquals(4, registerUserResponseModel.getId());
            Assertions.assertEquals("QpwL5tke4Pnpja7X4", registerUserResponseModel.getToken());
        });


    }

    @Test
    void getDelayedResponseTest() {

        given()
                .when()
                .contentType(ContentType.JSON)
                .get("/api/users?delay=3")

                .then()
                .log().body()
                .log().status()
                .statusCode(200);
    }
}
