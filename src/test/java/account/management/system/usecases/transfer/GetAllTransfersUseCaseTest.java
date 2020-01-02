package account.management.system.usecases.transfer;

import java.util.Arrays;
import java.util.Collections;

import account.management.system.model.Transfer;
import account.management.system.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class GetAllTransfersUseCaseTest {

	private TransferRepository transferRepository;
	private GetAllTransfersUseCase getAllTransfersUseCase;

	@BeforeEach
	void setUp() {
		transferRepository = mock(TransferRepository.class);
		getAllTransfersUseCase = new GetAllTransfersUseCase(transferRepository);
	}

	@Test
	void getEmpty() {
		// Arrange
		when(transferRepository.getAll()).thenReturn(Collections.emptyList());
		// Act & assert
		assertThat(getAllTransfersUseCase.getAll()).isNotNull()
		                                           .hasSize(0);
	}

	@Test
	void getCollection() {
		// Act
		Transfer firstTransfer = mock(Transfer.class);
		Transfer secondTransfer = mock(Transfer.class);
		when(transferRepository.getAll()).thenReturn(Arrays.asList(firstTransfer,
		                                                           secondTransfer));
		//Act & assert
		assertThat(getAllTransfersUseCase.getAll()).isNotNull()
		                                           .hasSize(2)
		                                           .containsExactly(firstTransfer, secondTransfer);

		verify(transferRepository).getAll();
	}
}