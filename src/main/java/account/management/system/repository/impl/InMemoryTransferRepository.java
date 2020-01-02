package account.management.system.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Singleton;

import account.management.system.model.Transfer;
import account.management.system.repository.TransferRepository;


@Singleton
public class InMemoryTransferRepository implements TransferRepository {

	private final ConcurrentHashMap<Long, Transfer> storage;
	private final AtomicLong idGenerator;

	public InMemoryTransferRepository() {
		this.storage = new ConcurrentHashMap<>();
		this.idGenerator = new AtomicLong();
	}

	@Override
	public Transfer getById(Long id) {
		return storage.get(id);
	}

	@Override
	public List<Transfer> getAll() {
		return new ArrayList<>(storage.values());
	}

	@Override
	public Transfer create(Long fromAccountId,
	                       Long toAccountId,
	                       BigDecimal value) {

		long id = idGenerator.incrementAndGet();
		Transfer transfer = Transfer.builder()
		                            .id(id)
		                            .fromAccountId(fromAccountId)
		                            .toAccountId(toAccountId)
		                            .value(value)
		                            .dateTime(new Date())
		                            .build();

		storage.put(id, transfer);
		return transfer;
	}
}
