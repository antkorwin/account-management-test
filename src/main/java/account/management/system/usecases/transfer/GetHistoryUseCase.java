package account.management.system.usecases.transfer;


import java.util.List;

import account.management.system.model.Transfer;
import account.management.system.repository.TransferRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class GetHistoryUseCase {

	private final TransferRepository transferRepository;

	public List<Transfer> getAll(){
		return transferRepository.getAll();
	}
}
