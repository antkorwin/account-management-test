package account.management.system.usecases.transfer;


import account.management.system.model.Transfer;
import account.management.system.repository.TransferRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;


@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GetTransferUseCase {

	private final TransferRepository transferRepository;

	public Transfer getTransfer(Long transferId) {
		Transfer transfer = transferRepository.getById(transferId);
		if (transfer == null) {
			throw new RuntimeException("Transfer not found.");
		}
		return transfer;
	}
}
