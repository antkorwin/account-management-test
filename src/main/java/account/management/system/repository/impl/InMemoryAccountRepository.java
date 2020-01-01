package account.management.system.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Singleton;

import account.management.system.model.Account;
import account.management.system.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Singleton
public class InMemoryAccountRepository implements AccountRepository {

	private final ConcurrentHashMap<Long, Account> storage;
	private final AtomicLong idGenerator;

	public InMemoryAccountRepository() {
		this.storage = new ConcurrentHashMap<>();
		this.idGenerator = new AtomicLong();
		log.info("create account repository");
	}


	@Override
	public Account getById(Long id) {
		return storage.get(id);
	}

	@Override
	public boolean isExist(Long id) {
		return storage.containsKey(id);
	}

	@Override
	public List<Account> getAll() {
		return new ArrayList<>(storage.values());
	}

	@Override
	public int size() {
		return storage.size();
	}

	@Override
	public Account create(String name, BigDecimal amount) {

		long id = idGenerator.incrementAndGet();
		Account account = Account.builder()
		                         .name(name)
		                         .balance(amount)
		                         .id(id)
		                         .build();

		storage.put(id, account);
		return account;
	}

	@Override
	public Account updateBalance(Long accountId, BigDecimal newValue) {
		Account account = storage.get(accountId);
		account.setBalance(newValue);
		return account;
	}
}
