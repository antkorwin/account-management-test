package account.management.system.usecases.account;


import java.util.List;

import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;


@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GetAllAccountsUseCase {

	private final AccountRepository accountRepository;

	public List<Account> getAll() {
		return accountRepository.getAll();
	}
}
