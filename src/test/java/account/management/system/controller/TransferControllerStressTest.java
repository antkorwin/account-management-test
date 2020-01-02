package account.management.system.controller;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import account.management.system.Application;
import account.management.system.controller.dto.in.CreateTransferDto;
import account.management.system.controller.dto.out.TransferDto;
import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import account.management.system.webserver.WebServer;
import com.jupiter.tools.stress.test.concurrency.ExecutionMode;
import com.jupiter.tools.stress.test.concurrency.StressTestRunner;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.undertow.util.StatusCodes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransferControllerStressTest {

	private static final int ITERATION_COUNT = 1000;
	private static final int THREAD_COUNT = 8;

	private AccountRepository accountRepository;

	@BeforeEach
	void setUp() {
		Application.main(new String[]{});
		accountRepository = Application.getInjector().getInstance(AccountRepository.class);
	}

	@AfterEach
	void tearDown() {
		Application.getInjector()
		           .getInstance(WebServer.class)
		           .stop();
	}

	@Test
	void stressTest() {
		Account firstAccount = accountRepository.create("first-name", BigDecimal.valueOf(1000));
		Account secondAccount = accountRepository.create("second-name", BigDecimal.valueOf(100));

		StressTestRunner.test()
		                .mode(ExecutionMode.EXECUTOR_MODE)
		                .iterations(ITERATION_COUNT)
		                .threads(THREAD_COUNT)
		                // prevents from deadlocks in tests
		                .timeout(1, TimeUnit.MINUTES)
		                // each iteration we select two random accounts and
		                // move AMOUNT of money from one account to another
		                .run(() -> {
			                Long firstAccountId = getRandomAccountId();
			                Long secondAccountId = getRandomAccountId(firstAccountId);

			                CreateTransferDto createDto = new CreateTransferDto(firstAccountId,
			                                                                    secondAccountId,
			                                                                    1L);

			                RestAssured.given()
			                           .contentType(ContentType.JSON)
			                           .body(createDto)
			                           .post("/transfers/create")
			                           .then()
			                           .statusCode(StatusCodes.CREATED)
			                           .extract()
			                           .as(TransferDto.class);
		                });

		BigDecimal balance = accountRepository.getById(firstAccount.getId()).getBalance()
		                                      .add(accountRepository.getById(secondAccount.getId()).getBalance());
		assertThat(balance).isEqualTo(BigDecimal.valueOf(1100));
	}

	private Long getRandomAccountId(Long... exclude) {
		int maxValue = accountRepository.size();
		Long rndIndex = (long) new Random().nextInt(maxValue) + 1;
		while (Arrays.asList(exclude).contains(rndIndex)) {
			rndIndex = (long) new Random().nextInt(maxValue) + 1;
		}
		return rndIndex;
	}
}
