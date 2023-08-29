import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import jdk.jfr.Description;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class LoginCourierTest {
    @Before
    public void setUp()
    {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @Description("SuccessfulLogin")
    public void SuccessfulLogin()
    {
        File createFile = new File("src/test/resources/CourierLoginPositive/PositiveCreateCourier.json");
        createCourier(createFile);
        File loginFile = new File("src/test/resources/CourierLoginPositive/PositiveLogin.json");
        deleteCourier(successfulLogin(loginFile));
    }
    @Test
    @Description("UnsuccessfulLoginMissingLogin")
    public void UnsuccessfulLoginMissingLogin()
    {
        File file = new File("src/test/resources/CourierLoginNegative/MissingLogin.json");
        unsuccessfulLogin(file);
    }
    @Test
    @Description("UnsuccessfulLoginMissingPassword")
    public void UnsuccessfulLoginMissingPassword()
    {
        File file = new File("src/test/resources/CourierLoginNegative/MissingPassword.json");
        unsuccessfulLogin(file);
    }

    @Test
    @Description("WrongPassword")
    public void WrongPassword()
    {
        File createFile = new File("src/test/resources/CourierLoginPositive/PositiveCreateCourier.json");
        createCourier(createFile);
        File wrongLogin = new File("src/test/resources/CourierLoginNegative/WrongPassword.json");
        unsuccessfulLoginWrongPassword(wrongLogin);
        File loginFile = new File("src/test/resources/CourierLoginPositive/PositiveLogin.json");
        deleteCourier(successfulLogin(loginFile));
    }

    @Step
    public String successfulLogin(File file) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(file)
                        .when()
                        .post("api/v1/courier/login");
        response.then().statusCode(200);
        return response.jsonPath().getString("id");

    }
    @Step
    public void createCourier(File file)
    {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(file)
                        .when()
                        .post("api/v1/courier");

        response.then().assertThat().statusCode(201).and().assertThat().body(equalTo("{\"ok\":true}"));
    }
    @Step
    public void deleteCourier(String id)
    {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/api/v1/courier/"+id);
        response.then().assertThat().statusCode(200);
    }

    @Step("UnsuccessfulLoginMissingParameter")
    public void unsuccessfulLogin(File file)
    {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(file)
                        .when()
                        .post("api/v1/courier/login");
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Step("UnsuccessfulLoginWrongPassword")
    public void unsuccessfulLoginWrongPassword(File file)
    {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(file)
                        .when()
                        .post("api/v1/courier/login");
        response.then().assertThat().statusCode(404).and().body("message", equalTo("Учетная запись не найдена"));
    }

}
