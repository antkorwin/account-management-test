package account.management.system.webserver;


import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WebServer {

	private final Undertow.Builder builder = Undertow.builder();
	private Undertow undertow;

	public WebServer() {
	}

	public WebServer hostPort(String host, int port) {
		builder.addHttpListener(port, host);
		return this;
	}

	public WebServer router(RoutingHandler router) {
		builder.setHandler(router);
		return this;
	}

	/**
	 * start HttpServer
	 */
	public WebServer start() {
		undertow = builder.build();
		undertow.start();
		printBanner();
		return this;
	}

	public WebServer stop() {
		if (undertow == null) {
			throw new RuntimeException("Server not started yet.");
		}
		undertow.stop();
		return this;
	}

	private void printBanner() {
		log.info("\n---------------------------------------------------------\n" +
		         "Successfully start web server at {}" +
		         "\n---------------------------------------------------------\n",
		         undertow.getListenerInfo().get(0).getAddress());
	}
}
