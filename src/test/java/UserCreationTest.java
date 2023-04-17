import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class UserCreationTest {
    private UserSteps userSteps;
    private User user;
    private String accessToken;

    @Before
    public void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {

        userSteps = new UserSteps();
        user = UserGenerator.getRandom();

    }

    @After
    public void cleanUp() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("It is possible to create user with valid data")
    public void createUserWithValidDataIsPossibleCheck() {
        ValidatableResponse response = userSteps.createUser(user)
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("accessToken", notNullValue());
        accessToken = response.extract().path("accessToken");
    }

    @Test
    @DisplayName("It is impossible to create user duplicate")
    public void createUserDoubleIsImpossibleCheck() {
        ValidatableResponse response = userSteps.createUser(user);
        userSteps.createUser(user)
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("message", equalTo("User already exists"));
        accessToken = response.extract().path("accessToken");

    }
}
