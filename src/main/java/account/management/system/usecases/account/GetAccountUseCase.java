package account.management.system.usecases.account;


import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;


@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GetAccountUseCase {

	private final AccountRepository accountRepository;

	public Account getAccount(Long accountId) {
		Account account = accountRepository.getById(accountId);
		if (account == null) {
			throw new RuntimeException("Account not found.");
		}
		return account;
	}
}
