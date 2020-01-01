package account.management.system.usecases.account;

import java.math.BigDecimal;

import account.management.system.model.Account;
import account.management.system.usecases.account.CreateAccountUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import account.management.system.repository.AccountRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CreateAccountUseCaseTest {

	private static final String NAME = "test-name";

	private CreateAccountUseCase createAccount;
	private AccountRepository accountRepository;

	@BeforeEach
	void setUp() {
		accountRepository = mock(AccountRepository.class);
		createAccount = new CreateAccountUseCase(accountRepository);
	}

	@Nested
	class SuccessfulCases {

		@Test
		void simpleCreate() {
			Account mockAccount = mock(Account.class);
			when(accountRepository.create(eq(NAME), eq(BigDecimal.ONE)))
					.thenReturn(mockAccount);
			// Act
			Account account = createAccount.create(NAME, BigDecimal.ONE);
			// Assert
			assertThat(account).isEqualTo(mockAccount);
			verify(accountRepository).create(eq(NAME), eq(BigDecimal.ONE));
		}

		@Test
		void zeroBalance() {
			Account mockAccount = mock(Account.class);
			when(accountRepository.create(eq(NAME), eq(new BigDecimal("0.00"))))
					.thenReturn(mockAccount);
			// Act
			Account account = createAccount.create(NAME, new BigDecimal("0.00"));
			// Assert
			assertThat(account).isEqualTo(mockAccount);
			verify(accountRepository).create(eq(NAME), eq(new BigDecimal("0.00")));
		}
	}


	@Nested
	class WrongNames {
		@Test
		void withoutName() {
			IllegalArgumentException ex =
					Assertions.assertThrows(IllegalArgumentException.class,
					                        () -> createAccount.create(null, BigDecimal.ONE));

			assertThat(ex.getMessage()).contains("Name of created account is a mandatory argument");
		}

		@Test
		void emptyName() {
			IllegalArgumentException ex =
					Assertions.assertThrows(IllegalArgumentException.class,
					                        () -> createAccount.create("", BigDecimal.ONE));

			assertThat(ex.getMessage()).contains("Name of created account shouldn't be empty.");
		}

		@Test
		void specialCharactersName() {
			IllegalArgumentException ex =
					Assertions.assertThrows(IllegalArgumentException.class,
					                        () -> createAccount.create(" \t", BigDecimal.ONE));

			assertThat(ex.getMessage()).contains("Name of created account shouldn't be empty.");
		}
	}

	@Nested
	class WrongBalance {

		@Test
		void withoutBalance() {
			IllegalArgumentException ex =
					Assertions.assertThrows(IllegalArgumentException.class,
					                        () -> createAccount.create(NAME, null));

			assertThat(ex.getMessage()).contains("Balance is a mandatory argument to create an account");
		}

		@Test
		void negativeBalance() {
			IllegalArgumentException ex =
					Assertions.assertThrows(IllegalArgumentException.class,
					                        () -> createAccount.create(NAME, BigDecimal.valueOf(-100)));

			assertThat(ex.getMessage()).contains("Balance should be greater or equal to 0.00");
		}
	}
}