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
import java.util.ArrayList;
import java.util.List;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {
    private UserSteps userSteps;
    private User user;
    private OrderSteps orderSteps;
    private Order order;
    private String accessToken;

    private final List<String> ingredients = new ArrayList<>();

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
        ingredients.add("61c0c5a71d1f82001bdaaa6c");
        ingredients.add("61c0c5a71d1f82001bdaaa75");
        order = new Order(ingredients);
        orderSteps.createOrder(order, accessToken);

    }

    @After
    public void cleanUp() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Get orders list as authorized user")
    public void getOrdersListForAuthorizedUserIsPossibleCheck() {
        orderSteps.getListOfOrders(accessToken)
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true), "orders", notNullValue());
    }

    @Test
    @DisplayName("Get orders list as unauthorized user")
    public void getOrdersListForUnauthorizedUserIsImpossibleCheck() {
        orderSteps.getListOfOrders(" ")
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("success", equalTo(false), "message", equalTo("You should be authorised"));
    }
}
