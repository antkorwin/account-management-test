package account.management.system.usecases.transfer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import account.management.system.repository.TransferRepository;
import account.management.system.repository.impl.InMemoryAccountRepository;
import account.management.system.repository.impl.InMemoryTransferRepository;
import com.antkorwin.xsync.XSync;
import com.jupiter.tools.stress.test.concurrency.ExecutionMode;
import com.jupiter.tools.stress.test.concurrency.StressTestRunner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
class CreateTransferUseCaseStressTest {

	private static final int ITERATION_COUNT = 100_000;
	private static final int THREAD_COUNT = 8;
	private static final long INITIAL_BALANCE = 1_000_000;
	private static final BigDecimal AMOUNT = BigDecimal.valueOf(10);

	private TransferRepository transferRepository;
	private AccountRepository accountRepository;
	private CreateTransferUseCase createTransferUseCase;

	@BeforeEach
	void setUp() {
		transferRepository = new InMemoryTransferRepository();
		accountRepository = new InMemoryAccountRepository();
		createTransferUseCase = new CreateTransferUseCase(transferRepository,
		                                                  accountRepository,
		                                                  new XSync<>());
		populateDataSet();
	}

	private void populateDataSet() {
		IntStream.range(0, 10)
		         .boxed()
		         .forEach(i -> accountRepository.create(i.toString(),
		                                                BigDecimal.valueOf(INITIAL_BALANCE)));
	}

	@Test
	void concurrentTest() {

		// making money transfers concurrently in different threads
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
			                createTransferUseCase.create(firstAccountId, secondAccountId, AMOUNT);
		                });

		// Assert the result
		assertThat(getTotalSumOfBalances()).isEqualTo(accountRepository.size() * INITIAL_BALANCE);
	}


	private Long getRandomAccountId(Long... exclude) {
		int maxValue = accountRepository.size();
		Long rndIndex = (long) new Random().nextInt(maxValue) + 1;
		while (Arrays.asList(exclude).contains(rndIndex)) {
			rndIndex = (long) new Random().nextInt(maxValue) + 1;
		}
		return rndIndex;
	}


	private long getTotalSumOfBalances() {
		List<Account> accounts = accountRepository.getAll();
		long sum = accounts.stream()
		                   .peek(a -> log.info("a[{}] = {}", a.getId(), a.getBalance()))
		                   .map(Account::getBalance)
		                   .mapToLong(BigDecimal::longValue)
		                   .sum();
		log.info("Total = {}", sum);
		return sum;
	}

}