import base.BaseRestClient;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;
import model.UserCredentials;
import static io.restassured.RestAssured.given;

public class UserSteps extends BaseRestClient {
    @Step("Create new user {user}")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(getReqSpec())
                .body(user)
                .when()
                .post(USER_REG)
                .then();
    }

    @Step("Delete user {user}")
    public ValidatableResponse deleteUser(String token) {
        return given()
                .spec(getReqSpec())
                .auth().oauth2(token)
                .when()
                .delete(USER)
                .then();
    }

    @Step("Login user {user}")
    public ValidatableResponse loginUser(UserCredentials userCredentials) {
        return given()
                .spec(getReqSpec())
                .body(userCredentials)
                .when()
                .post(USER_AUTH)
                .then();
    }

    @Step("Update user {user} data")
    public ValidatableResponse updateUserData(User user, String token) {
        return given()
                .spec(getReqSpec())
                .header("authorization", token)
                .and()
                .body(user)
                .when()
                .patch(USER)
                .then();
    }
}
