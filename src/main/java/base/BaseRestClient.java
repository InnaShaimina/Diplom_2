package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseRestClient {
    protected static final String BASE_URI = "https://stellarburgers.nomoreparties.site/api/";
    public static final String USER_AUTH = "/auth/login";
    public static final String USER = "/auth/user";
    public static final String ORDERS = "/orders";
    public static final String USER_REG = "/auth/register";
    protected RequestSpecification getReqSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(BASE_URI)
                .build();
    }
}
