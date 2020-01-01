package account.management.system.config;


import account.management.system.controller.mapper.AccountMapper;
import account.management.system.controller.mapper.AccountMapperImpl;
import account.management.system.repository.AccountRepository;
import account.management.system.repository.impl.InMemoryAccountRepository;
import com.google.inject.AbstractModule;

public class AccountModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AccountRepository.class).to(InMemoryAccountRepository.class);
		bind(AccountMapper.class).to(AccountMapperImpl.class);
	}

}
