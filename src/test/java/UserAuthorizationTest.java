import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import model.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserAuthorizationTest {
    private UserSteps userSteps;
    private User user;
    private UserCredentials userCredentials;
    private String accessToken;

    @Before
    public void globalSetUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Before
    public void setUp() {

        userSteps = new UserSteps();
        user = UserGenerator.getRandom();
        ValidatableResponse response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");

    }

    @After
    public void cleanUp() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("It is possible to login as existing user")
    public void loginAsUserWithValidDataIsPossibleCheck() {
        userSteps.loginUser(userCredentials.from(user))
                .assertThat()
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("It is impossible to login as non existing user")
    public void loginAsUserWithInvalidDataIsImpossibleCheck() {
        user.setEmail("nobody@nobody.ru");
        userSteps.loginUser(userCredentials.from(user))
                .assertThat()
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }
}
