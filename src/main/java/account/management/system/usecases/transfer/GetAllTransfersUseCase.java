package account.management.system.usecases.transfer;


import java.util.List;

import account.management.system.model.Transfer;
import account.management.system.repository.TransferRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;


@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class GetAllTransfersUseCase {

	private final TransferRepository transferRepository;

	public List<Transfer> getAll() {
		return transferRepository.getAll();
	}
}
