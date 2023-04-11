import base.BaseRestClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.Order;
import static io.restassured.RestAssured.given;

public class OrderSteps extends BaseRestClient {
    @Step("User creates new order")
    public ValidatableResponse createOrder(Order order, String accessToken) {
        return given()
                .spec(getReqSpec())
                .header("authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(ORDERS)
                .then();
    }

    @Step("Get user orders list")
    public ValidatableResponse getListOfOrders(String accessToken) {
        return given()
                .spec(getReqSpec())
                .header("authorization", accessToken)
                .when()
                .get(ORDERS)
                .then();
    }
}
