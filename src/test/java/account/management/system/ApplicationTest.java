package account.management.system;

import account.management.system.webserver.WebServer;
import io.restassured.RestAssured;
import io.undertow.util.StatusCodes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;


class ApplicationTest {

	@BeforeEach
	void setUp() {
		Application.main(new String[]{});
	}

	@AfterEach
	void tearDown() {
		Application.getInjector()
		           .getInstance(WebServer.class)
		           .stop();
	}

	@Test
	void healthCheck() {
		RestAssured.given()
		           .get("/health")
		           .then()
		           .statusCode(StatusCodes.OK)
		           .body(equalTo("up"));
	}
}