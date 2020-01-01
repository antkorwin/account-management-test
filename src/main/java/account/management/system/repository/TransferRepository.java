package account.management.system.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import account.management.system.model.Transfer;

public interface TransferRepository {

	Transfer getById(Long id);

	List<Transfer> getAll();

	Transfer create(Long fromAccountId,
	                Long toAccountId,
	                BigDecimal value);
}
