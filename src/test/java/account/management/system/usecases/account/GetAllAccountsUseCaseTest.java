package account.management.system.usecases.account;

import java.util.Arrays;
import java.util.List;

import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import account.management.system.usecases.account.GetAllAccountsUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetAllAccountsUseCaseTest {

	private AccountRepository accountRepository;
	private GetAllAccountsUseCase getAllAccountsUseCase;

	@BeforeEach
	void setUp() {
		accountRepository = mock(AccountRepository.class);
		getAllAccountsUseCase = new GetAllAccountsUseCase(accountRepository);
	}

	@Test
	void getAll() {
		// Arrange
		List<Account> mockList = Arrays.asList(mock(Account.class), mock(Account.class));
		when(accountRepository.getAll()).thenReturn(mockList);
		// Act
		List<Account> result = getAllAccountsUseCase.getAll();
		// Assert
		assertThat(result).isNotNull()
		                  .hasSize(2)
		                  .isEqualTo(mockList);
	}
}