package account.management.system.controller;


import account.management.system.webserver.Router;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Methods;
import lombok.RequiredArgsConstructor;

import static io.undertow.Handlers.routing;


@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class ApplicationRouter implements Router {

	private final AccountController accountController;
	private final TransferController transferController;

	public RoutingHandler getRoutes() {
		return routing()
				// accounts
				.add(Methods.POST, "/accounts/create", new BlockingHandler(accountController::create))
				.add(Methods.GET, "/accounts/{id}", new BlockingHandler(accountController::get))
				.add(Methods.GET, "/accounts/list", new BlockingHandler(accountController::getAll))
				// transfers
				.add(Methods.POST, "/transfers/create", new BlockingHandler(transferController::create));
	}
}
