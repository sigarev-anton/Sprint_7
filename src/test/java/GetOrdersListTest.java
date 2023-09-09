import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.notNullValue;
public class GetOrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }
    @Test
    @DisplayName("GetAllOrders")
    public void GetAllOrders()
    {
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
        response.then().assertThat().statusCode(200).and().body("orders", notNullValue());


    }
}
