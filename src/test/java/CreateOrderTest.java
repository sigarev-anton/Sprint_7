import com.thoughtworks.qdox.model.expression.Or;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import java.util.List;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    private final List<String> colorValue;
    public CreateOrderTest(List<String> colorValue) {
        this.colorValue = colorValue;
    }

    @Parameterized.Parameters
    public static Object[][] setOrderColor() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GRAY")},
                {List.of("BLACK", "GRAY")},
                {List.of()}
        };
    }
    @Test
    @DisplayName("CreateOrder")
    public void createOrder()
    {
        Order order = new Order( "Naruto", "Uchiha", "Konoha, 142 apt.", 4, "+7 800 355 35 35", "12", "2020-06-06", "Saske, come back to Konoha", colorValue);
        CreateOrder(order);
    }
    @Step("CreateOrder")
    public void CreateOrder(Order file)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(file)
                .when()
                .post("api/v1/orders");
        response.then().assertThat().statusCode(201).and().body("track", notNullValue());
    }

}
