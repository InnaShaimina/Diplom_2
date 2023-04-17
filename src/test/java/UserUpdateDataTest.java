import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class UserUpdateDataTest {
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
        ValidatableResponse response = userSteps.createUser(user);
        accessToken = response.extract().path("accessToken");

    }

    @After
    public void cleanUp() {
        userSteps.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Authorized user: update email")
    public void authUserCanUpdateEmailCheck() {
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userSteps.updateUserData(user, accessToken)
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Authorized user: update password")
    public void authUserCanUpdatePasswordCheck() {
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        userSteps.updateUserData(user, accessToken)
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Authorized user: update name")
    public void authUserCanUpdateNameCheck() {
        user.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.updateUserData(user, accessToken)
                .statusCode(HTTP_OK)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Unauthorized user: update email")
    public void unauthUserCanNotUpdateEmailCheck() {
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");
        userSteps.updateUserData(user, " ")
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Unauthorized user: update password")
    public void unauthUserCanNotUpdatePasswordCheck() {
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        userSteps.updateUserData(user, " ")
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Unauthorized user: update name")
    public void unauthUserCanNotUpdateNameCheck() {
        user.setName(RandomStringUtils.randomAlphabetic(10));
        userSteps.updateUserData(user, " ")
                .statusCode(HTTP_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
