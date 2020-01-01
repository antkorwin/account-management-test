package account.management.system.usecases.account;

import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import account.management.system.usecases.account.GetAccountUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetAccountUseCaseTest {

	private AccountRepository accountRepository;
	private GetAccountUseCase getAccountUseCase;

	@BeforeEach
	void setUp() {
		accountRepository = mock(AccountRepository.class);
		getAccountUseCase = new GetAccountUseCase(accountRepository);
	}

	@Test
	void successful() {
		// Arrange
		Long id = 123L;
		Account mockAccount = mock(Account.class);
		when(accountRepository.getById(eq(id))).thenReturn(mockAccount);
		// Act
		Account result = getAccountUseCase.getAccount(id);
		// Assert
		assertThat(result).isEqualTo(mockAccount);
		verify(accountRepository).getById(eq(id));
	}

	@Test
	void getNotExisted() {
		RuntimeException ex = assertThrows(RuntimeException.class,
		                                   () -> getAccountUseCase.getAccount(1100L));
		assertThat(ex.getMessage()).isEqualTo("Account not found.");
	}
}