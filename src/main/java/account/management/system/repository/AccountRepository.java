package account.management.system.repository;


import java.math.BigDecimal;
import java.util.List;

import account.management.system.model.Account;


public interface AccountRepository {

	/**
	 * Find the account by primary identifier.
	 *
	 * @param id of the necessary account.
	 * @return the account instance or null if not found.
	 */
	Account getById(Long id);

	/**
	 * Check existence of the account by the id
	 *
	 * @param id identifier of the account
	 * @return tru if exist and false if not
	 */
	boolean isExist(Long id);

	/**
	 * Get a list of all accounts for the storage
	 *
	 * @return list of accounts
	 */
	List<Account> getAll();

	/**
	 * Get the count of accounts in the storage
	 *
	 * @return size of the repository
	 */
	int size();

	/**
	 * Create and persist a new account instance
	 *
	 * @param name    account name
	 * @param balance account balance
	 * @return created account
	 */
	Account create(String name, BigDecimal balance);

	/**
	 * Set new value of a balance to selected account
	 *
	 * @param accountId id of account
	 * @param newValue  new value of a balance
	 * @return updated account
	 */
	Account updateBalance(Long accountId, BigDecimal newValue);
}
