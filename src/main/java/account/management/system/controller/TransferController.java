package account.management.system.controller;


import java.math.BigDecimal;

import account.management.system.controller.dto.in.CreateTransferDto;
import account.management.system.controller.mapper.TransferMapper;
import account.management.system.model.Transfer;
import account.management.system.usecases.transfer.CreateTransferUseCase;
import account.management.system.usecases.transfer.GetTransferUseCase;
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
public class TransferController {

	private final CreateTransferUseCase createTransferUseCase;
	private final GetTransferUseCase getTransferUseCase;
	private final ObjectMapper objectMapper;
	private final TransferMapper transferMapper;

	@SneakyThrows
	public void create(HttpServerExchange exchange) {

		CreateTransferDto createTransferDto =
				objectMapper.readValue(exchange.getInputStream(), CreateTransferDto.class);

		Transfer transfer = createTransferUseCase.create(createTransferDto.getFromAccountId(),
		                                                 createTransferDto.getToAccountId(),
		                                                 BigDecimal.valueOf(createTransferDto.getValue()));

		exchange.setStatusCode(StatusCodes.CREATED);
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(objectMapper.writeValueAsString(transferMapper.toDto(transfer)));
		log.info("Create new Transfer: {}", transfer);
	}

	@SneakyThrows
	public void get(HttpServerExchange exchange) {
		Long id = Long.valueOf(exchange.getQueryParameters().get("id").getFirst());
		Transfer transfer = getTransferUseCase.getTransfer(id);
		exchange.setStatusCode(StatusCodes.OK);
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
		exchange.getResponseSender().send(objectMapper.writeValueAsString(transferMapper.toDto(transfer)));
		log.info("Get Account : {}", transfer);
	}
}
