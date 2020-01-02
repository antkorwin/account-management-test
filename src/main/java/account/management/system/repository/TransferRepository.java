package account.management.system.repository;

import java.math.BigDecimal;
import java.util.List;

import account.management.system.model.Transfer;

public interface TransferRepository {

	/**
	 * Find a transfer instance by selected id
	 *
	 * @param id the identifier of transfer
	 * @return transfer instance
	 */
	Transfer getById(Long id);

	/**
	 * find all transfers from storage
	 *
	 * @return a list of transfers
	 */
	List<Transfer> getAll();

	/**
	 * Create a new transfer record in the storage
	 *
	 * @param fromAccountId source account id
	 * @param toAccountId   target account id
	 * @param value         amount of money which transferred from source to the target account
	 * @return the instance of created transfer
	 */
	Transfer create(Long fromAccountId,
	                Long toAccountId,
	                BigDecimal value);
}
