package account.management.system.repository.impl;

import account.management.system.model.Account;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;


class InMemoryAccountRepositoryTest {

	private InMemoryAccountRepository accountRepository;

	@BeforeEach
	void setUp() {
		accountRepository = new InMemoryAccountRepository();
	}

	@Test
	void create() {
		// Act
		Account account = accountRepository.create("test-name", valueOf(511));
		// Assert
		assertThat(account).isNotNull()
		                   .extracting(Account::getId,
		                               Account::getName,
		                               Account::getBalance)
		                   .containsExactly(1L,
		                                    "test-name",
		                                    valueOf(511));
	}

	@Test
	void getAll() {
		// Arrange
		accountRepository.create("first", valueOf(1111));
		accountRepository.create("second", valueOf(2222));
		// Act & assert
		assertThat(accountRepository.getAll())
				.hasSize(2)
				.extracting(Account::getId,
				            Account::getName,
				            Account::getBalance)
				.containsExactly(Tuple.tuple(1L, "first", valueOf(1111)),
				                 Tuple.tuple(2L, "second", valueOf(2222)));
	}

	@Test
	void getById() {
		// Arrange
		Account account = accountRepository.create("test-name", valueOf(123));
		// Act
		Account readAccount = accountRepository.getById(account.getId());
		// Assert
		assertThat(readAccount).isEqualTo(account);
	}

	@Test
	void findNotExisted() {
		// Act
		Account account = accountRepository.getById(123L);
		// Assert
		assertThat(account).isNull();
	}

	@Test
	void testExisting() {
		// Arrange
		Account first = accountRepository.create("first", valueOf(1111));
		Account second = accountRepository.create("second", valueOf(2222));
		// Act & assert
		assertThat(accountRepository.isExist(first.getId())).isEqualTo(true);
		assertThat(accountRepository.isExist(second.getId())).isEqualTo(true);
		assertThat(accountRepository.isExist(10000L)).isEqualTo(false);
	}


	@Test
	void updateBalance() {
		// Arrange
		Account first = accountRepository.create("first", valueOf(123));
		// Act
		accountRepository.updateBalance(first.getId(), valueOf(1000));
		// Arrange
		assertThat(accountRepository.getById(first.getId()).getBalance())
				.isEqualTo(valueOf(1000));
	}
}