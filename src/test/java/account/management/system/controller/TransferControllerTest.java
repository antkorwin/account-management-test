package account.management.system.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import account.management.system.Application;
import account.management.system.controller.dto.in.CreateTransferDto;
import account.management.system.controller.dto.out.TransferDto;
import account.management.system.model.Account;
import account.management.system.model.Transfer;
import account.management.system.repository.AccountRepository;
import account.management.system.repository.TransferRepository;
import account.management.system.webserver.WebServer;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.undertow.util.StatusCodes;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class TransferControllerTest {

	private TransferRepository transferRepository;
	private AccountRepository accountRepository;

	@BeforeEach
	void setUp() {
		Application.main(new String[]{});
		transferRepository = Application.getInjector().getInstance(TransferRepository.class);
		accountRepository = Application.getInjector().getInstance(AccountRepository.class);
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
		Account firstAccount = accountRepository.create("first-name", BigDecimal.valueOf(1000));
		Account secondAccount = accountRepository.create("second-name", BigDecimal.valueOf(100));
		CreateTransferDto createDto = new CreateTransferDto(firstAccount.getId(),
		                                                    secondAccount.getId(),
		                                                    1000L);
		// Act
		TransferDto result = RestAssured.given()
		                                .contentType(ContentType.JSON)
		                                .body(createDto)
		                                .post("/transfers/create")
		                                .then()
		                                .statusCode(StatusCodes.CREATED)
		                                .extract()
		                                .as(TransferDto.class);
		// Assert
		assertThat(result).isNotNull()
		                  .extracting(TransferDto::getId,
		                              TransferDto::getFromAccountId,
		                              TransferDto::getToAccountId,
		                              TransferDto::getValue)
		                  .containsExactly(1L,
		                                   firstAccount.getId(),
		                                   secondAccount.getId(),
		                                   BigDecimal.valueOf(1000));

		assertThat(transferRepository.getAll()).hasSize(1);
		assertAccountBalance(firstAccount.getId(), BigDecimal.ZERO);
		assertAccountBalance(secondAccount.getId(), BigDecimal.valueOf(1100));
	}

	@Test
	void createdTimeTest() {
		// Arrange
		Account firstAccount = accountRepository.create("first-name", BigDecimal.valueOf(1000));
		Account secondAccount = accountRepository.create("second-name", BigDecimal.valueOf(100));
		CreateTransferDto createDto = new CreateTransferDto(firstAccount.getId(),
		                                                    secondAccount.getId(),
		                                                    1000L);
		Date before = new Date();
		// Act
		Long dateTime = RestAssured.given()
		                             .contentType(ContentType.JSON)
		                             .body(createDto)
		                             .post("/transfers/create")
		                             .then()
		                             .extract()
		                             .path("dateTime");
		// Assert
		Date after = new Date();
		assertThat(new Date(dateTime)).isBetween(before, after, true, true);
	}

	@Test
	void get() {
		// Arrange
		Transfer transfer = transferRepository.create(1L,
		                                              2L,
		                                              BigDecimal.valueOf(1000));
		// Act
		TransferDto result = RestAssured.given()
		                                .get("/transfers/{id}", transfer.getId())
		                                .then()
		                                .statusCode(StatusCodes.OK)
		                                .extract()
		                                .as(TransferDto.class);
		// Assert
		assertThat(result).isNotNull()
		                  .extracting(TransferDto::getId,
		                              TransferDto::getFromAccountId,
		                              TransferDto::getToAccountId,
		                              TransferDto::getValue)
		                  .containsExactly(transfer.getId(),
		                                   1L,
		                                   2L,
		                                   BigDecimal.valueOf(1000));
	}

	@Test
	void getAll() {
		// Arrange
		transferRepository.create(1L, 3L, BigDecimal.valueOf(100));
		transferRepository.create(2L, 3L, BigDecimal.valueOf(1000));
		transferRepository.create(3L, 1L, BigDecimal.valueOf(700));
		// Act
		List<TransferDto> result = RestAssured.given()
		                                      .get("/transfers/list")
		                                      .then()
		                                      .statusCode(StatusCodes.OK)
		                                      .extract()
		                                      .as(new TypeRef<List<TransferDto>>() {
		                                      });
		// Assert
		assertThat(result).isNotNull()
		                  .hasSize(3)
		                  .extracting(TransferDto::getId,
		                              TransferDto::getFromAccountId,
		                              TransferDto::getToAccountId,
		                              TransferDto::getValue)
		                  .contains(Tuple.tuple(1L,
		                                        1L, 3L, BigDecimal.valueOf(100)),
		                            Tuple.tuple(2L,
		                                        2L, 3L, BigDecimal.valueOf(1000)),
		                            Tuple.tuple(3L,
		                                        3L, 1L, BigDecimal.valueOf(700))
		                           );
	}

	void assertAccountBalance(Long accountId, BigDecimal expectedValue) {
		Account account = accountRepository.getById(accountId);
		assertThat(account.getBalance()).isEqualTo(expectedValue);
	}
}