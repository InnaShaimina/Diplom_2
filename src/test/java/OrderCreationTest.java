import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import model.Order;
import model.User;
import model.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import java.util.ArrayList;
import java.util.List;

public class OrderCreationTest {
    private UserSteps userSteps;
    private User user;
    private OrderSteps orderSteps;
    private Order order;
    private String accessToken;

    private List<String> ingredients = new ArrayList<>();

    @Before
    public void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {

        userSteps = new UserSteps();
        user = UserGenerator.getRandom();
        orderSteps = new OrderSteps();
        accessToken = userSteps.createUser(user).extract().path("accessToken");

    }

    @After
    public void cleanUp() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("It is possible to create new order with ingredients by authorized user")
    public void createOrderByAuthorizedUserWithIngredientsIsPossibleCheck() {
        ingredients.add("61c0c5a71d1f82001bdaaa6c");
        ingredients.add("61c0c5a71d1f82001bdaaa75");
        order = new Order(ingredients);
        orderSteps.createOrder(order, accessToken)
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true), "order.number", notNullValue());
    }

    @Test
    @DisplayName("It is possible to create new order with ingredients by unauthorized user")
    public void createOrderByUnauthorizedUserWithIngredientsIsPossibleCheck() {
        ingredients.add("61c0c5a71d1f82001bdaaa6c");
        ingredients.add("61c0c5a71d1f82001bdaaa75");
        order = new Order(ingredients);
        orderSteps.createOrder(order, " ")
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true), "order.number", notNullValue());
    }

    @Test
    @DisplayName("It is impossible to create new order without ingredients by authorized user")
    public void createOrderByAuthorizedUserWithoutIngredientsIsImpossibleCheck() {
        order = new Order(ingredients);
        orderSteps.createOrder(order, accessToken)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("success", equalTo(false), "message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("It is impossible to create new order with wrong ingredient id by unauthorized user")
    public void createOrderByUnauthorizedUserWithWrongIngredientIsImpossibleCheck() {
        ingredients.add("WRONG_HASH");
        order = new Order(ingredients);
        orderSteps.createOrder(order, "WRONG_HASH")
                .assertThat()
                .statusCode(HTTP_INTERNAL_ERROR);
    }
}
