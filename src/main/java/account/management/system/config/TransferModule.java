package account.management.system.config;

import account.management.system.controller.mapper.TransferMapper;
import account.management.system.controller.mapper.TransferMapperImpl;
import account.management.system.repository.TransferRepository;
import account.management.system.repository.impl.InMemoryTransferRepository;
import com.google.inject.AbstractModule;

public class TransferModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(TransferRepository.class).to(InMemoryTransferRepository.class);
		bind(TransferMapper.class).to(TransferMapperImpl.class);
	}
}
