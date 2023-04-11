import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class UserCreationParametrizedTest {
    private UserSteps userSteps;
    private User user;

    public UserCreationParametrizedTest(User user){
        this.user = user;
    }

    @Parameterized.Parameters
    public static Object[][]getData(){
        return new Object[][]{
                {new User(null,"123456","Invalid")},
                {new User(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru",null,"Nobody")},
                {new User(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru","Qwerty123",null)}
        };
    }

    @Before
    public void setUp() {

        userSteps = new UserSteps();

    }

    @Test
    @DisplayName("Login and password and name fields are required to create user")
    public void createUserWithoutRequiredFieldsIsImpossibleCheck() {
        userSteps.createUser(user)
                .assertThat()
                .statusCode(HTTP_FORBIDDEN)
                .and()
                .body("message",equalTo("Email, password and name are required fields"));
    }
}
