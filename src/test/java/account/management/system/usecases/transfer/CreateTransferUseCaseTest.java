package account.management.system.usecases.transfer;

import java.math.BigDecimal;

import account.management.system.model.Account;
import account.management.system.model.Transfer;
import account.management.system.repository.AccountRepository;
import account.management.system.repository.TransferRepository;
import com.antkorwin.xsync.XSync;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateTransferUseCaseTest {

	private static final long FROM_ACCOUNT_ID = 1L;
	private static final long TO_ACCOUNT_ID = 2L;

	private static final Account FROM_ACCOUNT = Account.builder()
	                                                   .id(FROM_ACCOUNT_ID)
	                                                   .balance(BigDecimal.valueOf(1000))
	                                                   .name("from")
	                                                   .build();

	private static final Account TO_ACCOUNT = Account.builder()
	                                                 .id(TO_ACCOUNT_ID)
	                                                 .balance(BigDecimal.valueOf(100))
	                                                 .name("to")
	                                                 .build();

	private static final BigDecimal FROM_ACCOUNT_BALANCE = BigDecimal.valueOf(1000);
	private static final BigDecimal TO_ACCOUNT_BALANCE = BigDecimal.valueOf(100);

	private TransferRepository transferRepository = mock(TransferRepository.class);
	private AccountRepository accountRepository = mock(AccountRepository.class);
	private XSync<Long> xsync = new XSync<>();

	private CreateTransferUseCase createTransferUseCase = new CreateTransferUseCase(transferRepository,
	                                                                                accountRepository,
	                                                                                xsync);

	@Test
	void createTransfer() {
		when(accountRepository.getById(FROM_ACCOUNT_ID)).thenReturn(FROM_ACCOUNT);
		when(accountRepository.getById(TO_ACCOUNT_ID)).thenReturn(TO_ACCOUNT);
		Transfer transferMock = mock(Transfer.class);
		when(transferRepository.create(eq(FROM_ACCOUNT_ID),
		                               eq(TO_ACCOUNT_ID),
		                               eq(FROM_ACCOUNT_BALANCE))).thenReturn(transferMock);
		// Act
		Transfer transfer = createTransferUseCase.create(FROM_ACCOUNT_ID,
		                                                 TO_ACCOUNT_ID,
		                                                 FROM_ACCOUNT_BALANCE);
		// Assert
		assertThat(transfer).isNotNull()
		                    .isEqualTo(transferMock);

		verify(accountRepository).updateBalance(eq(FROM_ACCOUNT_ID), eq(BigDecimal.ZERO));
		verify(accountRepository).updateBalance(eq(TO_ACCOUNT_ID),
		                                        eq(TO_ACCOUNT_BALANCE.add(FROM_ACCOUNT_BALANCE)));
	}

	@Test
	void notFoundSourceAccount() {
		when(accountRepository.getById(FROM_ACCOUNT_ID)).thenReturn(null);
		when(accountRepository.getById(TO_ACCOUNT_ID)).thenReturn(TO_ACCOUNT);
		// Act
		RuntimeException ex =
				Assertions.assertThrows(RuntimeException.class,
				                        () -> createTransferUseCase.create(FROM_ACCOUNT_ID,
				                                                           TO_ACCOUNT_ID,
				                                                           FROM_ACCOUNT_BALANCE));
		// Assert
		assertThat(ex.getMessage()).isEqualTo("Source account not found");
	}

	@Test
	void notFoundTargetAccount() {
		when(accountRepository.getById(FROM_ACCOUNT_ID)).thenReturn(FROM_ACCOUNT);
		when(accountRepository.getById(TO_ACCOUNT_ID)).thenReturn(null);
		// Act
		RuntimeException ex =
				Assertions.assertThrows(RuntimeException.class,
				                        () -> createTransferUseCase.create(FROM_ACCOUNT_ID,
				                                                           TO_ACCOUNT_ID,
				                                                           FROM_ACCOUNT_BALANCE));
		// Assert
		assertThat(ex.getMessage()).isEqualTo("Target account not found");
	}

	@Test
	void notEnoughMoney() {
		// Arrange
		when(accountRepository.getById(FROM_ACCOUNT_ID)).thenReturn(FROM_ACCOUNT);
		when(accountRepository.getById(TO_ACCOUNT_ID)).thenReturn(TO_ACCOUNT);
		BigDecimal moreThanFromBalance = FROM_ACCOUNT_BALANCE.add(BigDecimal.valueOf(10));
		// Act
		RuntimeException ex =
				Assertions.assertThrows(RuntimeException.class,
				                        () -> createTransferUseCase.create(FROM_ACCOUNT_ID,
				                                                           TO_ACCOUNT_ID,
				                                                           moreThanFromBalance));
		// Assert
		assertThat(ex.getMessage()).isEqualTo("There is not enough money on the source account balance.");
	}
}