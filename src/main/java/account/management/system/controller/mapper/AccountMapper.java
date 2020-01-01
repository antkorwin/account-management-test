package account.management.system.controller.mapper;


import java.util.List;

import account.management.system.controller.dto.out.AccountDto;
import account.management.system.model.Account;
import org.mapstruct.Mapper;

@Mapper
public interface AccountMapper {

	AccountDto toDto(Account account);

	List<AccountDto> toListDto(List<Account> accounts);
}
