package account.management.system;

import java.math.BigDecimal;

import account.management.system.controller.dto.in.CreateAccountDto;
import account.management.system.controller.dto.out.AccountDto;
import account.management.system.webserver.WebServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.undertow.util.StatusCodes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class ApplicationTest {

	private static final String TEST_NAME = "test-name";
	private static final BigDecimal TEST_BALANCE = BigDecimal.valueOf(1000);

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
	void create() {
		// Arrange
		CreateAccountDto createDto = new CreateAccountDto(TEST_NAME, TEST_BALANCE.longValue());
		// Act
		AccountDto result = RestAssured.given()
		                               .contentType(ContentType.JSON)
		                               .body(createDto)
		                               .post("/accounts/create")
		                               .then()
		                               .statusCode(StatusCodes.CREATED)
		                               .extract()
		                               .as(AccountDto.class);
		// Assert
		assertThat(result).isNotNull()
		                  .extracting(AccountDto::getName, AccountDto::getBalance)
		                  .contains(TEST_NAME, TEST_BALANCE);
	}
}