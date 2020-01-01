package account.management.system.usecases.transfer;

import account.management.system.model.Transfer;
import account.management.system.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetTransferUseCaseTest {

	private TransferRepository transferRepository;
	private GetTransferUseCase getTransferUseCase;

	@BeforeEach
	void setUp() {
		transferRepository = mock(TransferRepository.class);
		getTransferUseCase = new GetTransferUseCase(transferRepository);
	}

	@Test
	void getExisted() {
		// Arrange
		Long id = 123L;
		Transfer mockTransfer = mock(Transfer.class);
		when(transferRepository.getById(eq(id))).thenReturn(mockTransfer);
		// Act
		Transfer result = getTransferUseCase.getTransfer(id);
		// Assert
		assertThat(result).isEqualTo(mockTransfer);
		verify(transferRepository).getById(eq(id));
	}

	@Test
	void getNotExisted() {
		RuntimeException ex = assertThrows(RuntimeException.class,
		                                   () -> getTransferUseCase.getTransfer(123L));
		assertThat(ex.getMessage()).isEqualTo("Transfer not found.");
	}

}