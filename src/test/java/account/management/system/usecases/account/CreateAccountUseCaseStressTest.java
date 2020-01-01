package account.management.system.usecases.account;

import java.math.BigDecimal;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.LongStream;

import account.management.system.usecases.account.CreateAccountUseCase;
import com.jupiter.tools.stress.test.concurrency.ExecutionMode;
import com.jupiter.tools.stress.test.concurrency.StressTestRunner;
import account.management.system.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import account.management.system.repository.AccountRepository;
import account.management.system.repository.impl.InMemoryAccountRepository;

import static org.assertj.core.api.Assertions.assertThat;


class CreateAccountUseCaseStressTest {

	private static final int COUNT_OF_CREATED_ACCOUNT = 100_000;

	private CreateAccountUseCase createAccount;

	@BeforeEach
	void setUp() {
		AccountRepository accountRepository = new InMemoryAccountRepository();
		createAccount = new CreateAccountUseCase(accountRepository);
	}

	@Test
	void stressTest() {
		// Arrange
		Set<Long> expectedIds = ConcurrentHashMap.newKeySet();
		LongStream.range(1, COUNT_OF_CREATED_ACCOUNT)
		          .boxed()
		          .forEach(expectedIds::add);
		// Act
		StressTestRunner.test()
		                .mode(ExecutionMode.EXECUTOR_MODE)
		                .threads(8)
		                .iterations(COUNT_OF_CREATED_ACCOUNT)
		                .run(() -> {
			                Account account = createAccount.create("Yeah!", BigDecimal.ZERO);
			                expectedIds.remove(account.getId());
		                });
		// Assert
		assertThat(expectedIds).hasSize(0);
	}
}