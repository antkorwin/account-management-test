package account.management.system.controller.routing;

import account.management.system.controller.dto.out.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import lombok.RequiredArgsConstructor;


@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ApiHandler {

	private final ObjectMapper objectMapper;

	/**
	 * Wrap handler in the ExceptionHandler and BlockingHandler
	 *
	 * @param handler source http handler
	 * @return wrapped exception
	 */
	public HttpHandler wrap(HttpHandler handler) {
		ExceptionHandler exceptionHandler = new ExceptionHandler(handler);
		exceptionHandler.addExceptionHandler(RuntimeException.class, errorHandler());
		return new BlockingHandler(exceptionHandler);
	}

	/**
	 * Api Exception Handler processing,
	 * wrap an exception message in the {@link ErrorDto}
	 */
	private HttpHandler errorHandler() {
		return exchange -> {
			Throwable throwable = exchange.getAttachment(ExceptionHandler.THROWABLE);
			ErrorDto errorDto = new ErrorDto(throwable.getMessage());
			exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
			exchange.getResponseSender().send(objectMapper.writeValueAsString(errorDto));
		};
	}
}
