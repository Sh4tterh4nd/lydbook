package ch.fhnw.webec;


import io.restassured.RestAssured;
import io.restassured.authentication.FormAuthConfig;
import io.restassured.filter.session.SessionFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AdminControllerTest {
    private static final String ADMIN = "admin";
    private static final String ADMIN_PW = "admin";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

//    @Test
//    public void testAdminPermissionOnAdminSection() throws InterruptedException {
//        SessionFilter sessionFilter = new SessionFilter();
//        RestAssured.given()
//                .auth()
//                .form(ADMIN, ADMIN_PW, new FormAuthConfig("/login","username","password")
//                        .withAutoDetectionOfCsrf()
//                        .withLoggingEnabled())
//                .when()
//                .get("/")
//                .print();
//
//        Thread.sleep(15000);
//    }

}
