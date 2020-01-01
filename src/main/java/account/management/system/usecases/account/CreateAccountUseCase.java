package account.management.system.usecases.account;


import java.math.BigDecimal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;


@Singleton
@RequiredArgsConstructor(onConstructor_={@Inject})
public class CreateAccountUseCase {

	private final AccountRepository accountRepository;

	public Account create(String name, BigDecimal balance) {
		validateName(name);
		validateBalance(balance);
		return accountRepository.create(name, balance);
	}

	private void validateBalance(BigDecimal balance) {
		if (balance == null) {
			throw new IllegalArgumentException("Balance is a mandatory argument to create an account");
		}
		if (balance.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Balance should be greater or equal to 0.00");
		}
	}

	private void validateName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name of created account is a mandatory argument.");
		}
		if (name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name of created account shouldn't be empty.");
		}
	}
}
