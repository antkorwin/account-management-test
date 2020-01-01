package account.management.system.repository.impl;

import java.math.BigDecimal;
import java.util.Date;

import account.management.system.model.Transfer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTransferRepositoryTest {

	private InMemoryTransferRepository repository;
	private static final long FROM_ACCOUNT_ID = 1L;
	private static final long TO_ACCOUNT_ID = 2L;
	private static final BigDecimal VALUE = BigDecimal.valueOf(1000);

	@BeforeEach
	void setUp() {
		repository = new InMemoryTransferRepository();
	}

	@Test
	void create() {
		// Act
		Transfer transfer = repository.create(FROM_ACCOUNT_ID,
		                                      TO_ACCOUNT_ID,
		                                      VALUE);
		// Assert
		assertThat(transfer).isNotNull()
		                    .extracting(Transfer::getId,
		                                Transfer::getFromAccountId,
		                                Transfer::getToAccountId,
		                                Transfer::getValue)
		                    .containsExactly(1L,
		                                     FROM_ACCOUNT_ID,
		                                     TO_ACCOUNT_ID,
		                                     VALUE);

		assertThat(repository.getAll()).hasSize(1);
	}

	@Test
	void dateTimeGenerationInCreate() {
		Date dateBefore = new Date();
		// Act
		Transfer transfer = repository.create(FROM_ACCOUNT_ID,
		                                      TO_ACCOUNT_ID,
		                                      VALUE);
		// Assert
		Date dateAfter = new Date();
		assertThat(transfer.getDateTime())
				.isBetween(dateBefore, dateAfter, true, true);
	}

	@Test
	void getAll() {
		// Act
		repository.create(FROM_ACCOUNT_ID,
		                  TO_ACCOUNT_ID,
		                  VALUE);

		repository.create(TO_ACCOUNT_ID,
		                  FROM_ACCOUNT_ID,
		                  VALUE);
		// Assert
		assertThat(repository.getAll()).hasSize(2)
		                               .extracting(Transfer::getId)
		                               .containsExactly(1L, 2L);
	}

	@Test
	void getById() {
		// Arrange
		Transfer transfer = repository.create(FROM_ACCOUNT_ID,
		                                      TO_ACCOUNT_ID,
		                                      VALUE);
		// Act
		Transfer readTransfer = repository.getById(transfer.getId());
		// Assert
		assertThat(readTransfer).isEqualTo(transfer);
	}
}