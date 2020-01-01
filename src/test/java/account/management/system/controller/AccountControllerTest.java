package account.management.system.controller;

import java.math.BigDecimal;
import java.util.List;

import account.management.system.config.AccountModule;
import account.management.system.controller.dto.in.CreateAccountDto;
import account.management.system.controller.dto.out.AccountDto;
import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import account.management.system.webserver.WebServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Methods;
import io.undertow.util.StatusCodes;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.undertow.Handlers.routing;
import static org.assertj.core.api.Assertions.assertThat;


class AccountControllerTest {

	private static final String TEST_NAME = "test-name";
	private static final BigDecimal TEST_BALANCE = BigDecimal.valueOf(1000);
	private static final String SECOND_TEST_NAME = "second-test-name";
	private static final BigDecimal SECOND_TEST_BALANCE = BigDecimal.valueOf(100);

	private AccountController controller;
	private AccountRepository repository;
	private WebServer server;

	@BeforeEach
	void setUp() {
		Injector injector = Guice.createInjector(new AccountModule());
		controller = injector.getInstance(AccountController.class);
		repository = injector.getInstance(AccountRepository.class);

		server = new WebServer().hostPort("localhost", 8080)
		                        .router(routing().add(Methods.POST, "/accounts/create", new BlockingHandler(controller::create))
		                                         .add(Methods.GET, "/accounts/{id}", new BlockingHandler(controller::get))
		                                         .add(Methods.GET, "/accounts/list", new BlockingHandler(controller::getAll)))
		                        .start();
	}

	@AfterEach
	void tearDown() {
		server.stop();
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

		assertThat(repository.getAll()).hasSize(1)
		                               .extracting(Account::getId)
		                               .contains(result.getId());
	}

	@Test
	void get() {
		// Arrange
		repository.create(TEST_NAME, TEST_BALANCE);
		// Act
		AccountDto result = RestAssured.given()
		                               .get("/accounts/{id}", 1)
		                               .then()
		                               .statusCode(StatusCodes.OK)
		                               .extract()
		                               .as(AccountDto.class);
		// Assert
		assertThat(result).isNotNull()
		                  .extracting(AccountDto::getName, AccountDto::getBalance)
		                  .contains(TEST_NAME, TEST_BALANCE);
	}

	@Test
	void getAll() {
		// Arrange
		repository.create(TEST_NAME, TEST_BALANCE);
		repository.create(SECOND_TEST_NAME, SECOND_TEST_BALANCE);
		// Act
		List<AccountDto> result = RestAssured.given()
		                                     .get("/accounts/list")
		                                     .then()
		                                     .statusCode(StatusCodes.OK)
		                                     .extract()
		                                     .as(new TypeRef<List<AccountDto>>() {});
		// Assert
		System.out.println(result);
		assertThat(result).isNotNull()
		                  .hasSize(2)
		                  .extracting(AccountDto::getName, AccountDto::getBalance)
		                  .contains(Tuple.tuple(TEST_NAME, TEST_BALANCE),
		                            Tuple.tuple(SECOND_TEST_NAME, SECOND_TEST_BALANCE));
	}

}