package account.management.system.webserver;

import java.net.ConnectException;

import io.restassured.RestAssured;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Methods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.undertow.Handlers.routing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;


class WebServerTest {

	@Test
	void startWebServer() {
		HttpHandler test = exchange -> exchange.getResponseSender().send("Hello 2020!");

		WebServer server = new WebServer().hostPort("127.0.0.1", 8080)
		                                  .router(routing().add(Methods.GET, "/test", new BlockingHandler(test)))
		                                  .start();

		RestAssured.given()
		           .get("/test")
		           .then()
		           .assertThat()
		           .body(equalTo("Hello 2020!"))
		           .statusCode(200);

		server.stop();
	}

	@Test
	void stopWebServer() {

		HttpHandler test = exchange -> exchange.getResponseSender().send("Hello 2020!");

		new WebServer().hostPort("127.0.0.1", 8080)
		               .router(routing().add(Methods.GET, "/tst", new BlockingHandler(test)))
		               .start()
		               .stop();

		ConnectException ex = Assertions.assertThrows(ConnectException.class,
		                                              () -> RestAssured.get("/tst"));
		assertThat(ex.getMessage()).contains("Connection refused");
	}

}