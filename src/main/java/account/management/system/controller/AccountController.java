package account.management.system.controller;

import java.math.BigDecimal;
import java.util.List;

import account.management.system.controller.dto.in.CreateAccountDto;
import account.management.system.controller.dto.out.AccountDto;
import account.management.system.controller.mapper.AccountMapper;
import account.management.system.model.Account;
import account.management.system.usecases.account.CreateAccountUseCase;
import account.management.system.usecases.account.GetAccountUseCase;
import account.management.system.usecases.account.GetAllAccountsUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class AccountController {

	private final CreateAccountUseCase createAccountUseCase;
	private final GetAccountUseCase getAccountUseCase;
	private final GetAllAccountsUseCase getAllAccountsUseCase;
	private final AccountMapper accountMapper;
	private final ObjectMapper objectMapper;

	@SneakyThrows
	public void create(HttpServerExchange exchange) {

		CreateAccountDto createAccountDto =
				objectMapper.readValue(exchange.getInputStream(), CreateAccountDto.class);

		Account account = createAccountUseCase.create(createAccountDto.getName(),
		                                              BigDecimal.valueOf(createAccountDto.getBalance()));

		exchange.setStatusCode(StatusCodes.CREATED);
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(objectMapper.writeValueAsString(accountMapper.toDto(account)));
		log.info("Created new Account: {}", account);
	}

	@SneakyThrows
	public void get(HttpServerExchange exchange) {
		Long id = Long.valueOf(exchange.getQueryParameters().get("id").getFirst());
		Account account = getAccountUseCase.getAccount(id);
		exchange.setStatusCode(StatusCodes.OK);
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(objectMapper.writeValueAsString(accountMapper.toDto(account)));
		log.info("Get Account : {}", account);
	}

	@SneakyThrows
	public void getAll(HttpServerExchange exchange) {
		List<AccountDto> result = accountMapper.toListDto(getAllAccountsUseCase.getAll());
		exchange.setStatusCode(StatusCodes.OK);
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(objectMapper.writeValueAsString(result));
		log.info("Get All Accounts: {}", result);
	}
}
