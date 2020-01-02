package account.management.system.webserver;

import io.undertow.server.RoutingHandler;

public interface Router {

	RoutingHandler getRoutes();
}
