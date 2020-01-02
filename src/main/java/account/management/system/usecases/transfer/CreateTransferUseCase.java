package account.management.system.usecases.transfer;


import java.math.BigDecimal;

import account.management.system.model.Account;
import account.management.system.model.Transfer;
import account.management.system.repository.AccountRepository;
import account.management.system.repository.TransferRepository;
import com.antkorwin.xsync.XSync;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class CreateTransferUseCase {

	private final TransferRepository transferRepository;
	private final AccountRepository accountRepository;
	private final XSync<Long> xsync;

	public Transfer create(Long fromAccountId,
	                       Long toAccountId,
	                       BigDecimal value) {

		return xsync.evaluate(fromAccountId, toAccountId, () -> {

			Account source = accountRepository.getById(fromAccountId);
			validateSourceAccount(source, value);
			decreaseBalance(source, value);

			Account target = accountRepository.getById(toAccountId);
			validateTargetAccount(target);
			increaseBalance(target, value);

			return transferRepository.create(fromAccountId, toAccountId, value);
		});
	}

	private void decreaseBalance(Account sourceAccount, BigDecimal value) {
		BigDecimal balanceBefore = sourceAccount.getBalance();
		BigDecimal balanceAfter = balanceBefore.subtract(value);
		accountRepository.updateBalance(sourceAccount.getId(), balanceAfter);
	}

	private void increaseBalance(Account targetAccount, BigDecimal value) {
		BigDecimal balanceBefore = targetAccount.getBalance();
		BigDecimal balanceAfter = balanceBefore.add(value);
		accountRepository.updateBalance(targetAccount.getId(), balanceAfter);
	}

	private void validateSourceAccount(Account sourceAccount, BigDecimal value) {
		if (sourceAccount == null) {
			throw new RuntimeException("Source account not found");
		}
		if (sourceAccount.getBalance().compareTo(value) < 0) {
			throw new RuntimeException("There is not enough money on the source account balance.");
		}
	}

	private void validateTargetAccount(Account destinationAccount) {
		if (destinationAccount == null) {
			throw new RuntimeException("Target account not found");
		}
	}
}
