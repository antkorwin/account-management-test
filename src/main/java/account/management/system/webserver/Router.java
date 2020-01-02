package account.management.system.webserver;

import io.undertow.server.RoutingHandler;

/**
 * WebServer routing handlers provider
 */
public interface Router {

	/**
	 * return all routing handlers for WebServer
	 * @return RoutingHandler
	 */
	RoutingHandler getRoutes();
}
