package account.management.system.usecases.account;


import java.util.List;

import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GetAllAccountsUseCase {

	private final AccountRepository accountRepository;

	public List<Account> getAll(){
		return accountRepository.getAll();
	}
}
