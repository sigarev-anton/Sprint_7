import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Create new courier, login and delete")
    public void createNewCourier() {
        File person = new File("src/test/resources/CourierPositiveCases/CourierPositive.json");
        File login = new File("src/test/resources/CourierPositiveCases/login.json");
        CreateCourier(person);
        String id = loginCourier(login);
        deleteCourier(id);
    }

  @Test
   @DisplayName("Try to create duplicate courier")
   public void duplicateCourier()
  {
        File person = new File("src/test/resources/CourierNegativeCases/DoubleCourier.json");
        File login = new File("src/test/resources/CourierNegativeCases/duplicateLogin.json");
        CreateCourier(person);
        CreateDuplicate(person);
        String id = loginCourier(login);
        System.out.println(id);
        deleteCourier(id);
  }

  @Test
  @DisplayName("Try to create courier with missing login")
  public void missingLogin()
  {
      File file = new File("src/test/resources/CourierNegativeCases/NoLogin.json");
      CreateCourierMissingParameters(file);
  }
    @Test
    @DisplayName("Try to create courier with missing password")
    public void missingPassword()
    {
        File file = new File("src/test/resources/CourierNegativeCases/NoPassword.json");
        CreateCourierMissingParameters(file);
    }



    @Step("loginCourier")
    public String loginCourier(File json)
    {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("api/v1/courier/login");
        return response.jsonPath().getString("id");
    }
    @Step("DeleteCourier")
    public void deleteCourier(String id)
    {
               Response response = given()
               .header("Content-type", "application/json")
                        .when()
                        .delete("/api/v1/courier/"+id);
               response.then().assertThat().statusCode(200);

    }
    @Step("CreateCourierPositive")
    public void CreateCourier(File json)
    {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("api/v1/courier");

                        response.then().assertThat().statusCode(201).and().assertThat().body(equalTo("{\"ok\":true}"));
    }
    @Step
    public void CreateDuplicate(File json)
    {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("api/v1/courier");
                response.then().assertThat().statusCode(409);//.and().assertThat().body(equalTo("{\"code\":409,\"message\":\"Этот логин уже используется. Попробуйте другой.\"}"));
    }

    @Step("CreateCourierMissingParameters")
    public void CreateCourierMissingParameters(File json)
    {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("api/v1/courier");
            response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
            System.out.println(response);
    }

}

