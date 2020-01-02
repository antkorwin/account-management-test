package account.management.system.controller;

import java.math.BigDecimal;

import account.management.system.Application;
import account.management.system.controller.dto.in.CreateTransferDto;
import account.management.system.controller.dto.out.TransferDto;
import account.management.system.model.Account;
import account.management.system.model.Transfer;
import account.management.system.repository.AccountRepository;
import account.management.system.repository.TransferRepository;
import account.management.system.webserver.WebServer;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.undertow.util.StatusCodes;
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
	void get() {
		// Arrange
		Account firstAccount = accountRepository.create("first-name", BigDecimal.valueOf(1000));
		Account secondAccount = accountRepository.create("second-name", BigDecimal.valueOf(100));
		Transfer transfer = transferRepository.create(firstAccount.getId(),
		                                              secondAccount.getId(),
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
		                                   firstAccount.getId(),
		                                   secondAccount.getId(),
		                                   BigDecimal.valueOf(1000));
	}

	void assertAccountBalance(Long accountId, BigDecimal expectedValue) {
		Account account = accountRepository.getById(accountId);
		assertThat(account.getBalance()).isEqualTo(expectedValue);
	}
}