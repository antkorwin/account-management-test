package account.management.system.webserver;


import java.net.SocketAddress;

import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class WebServer {

	private final Undertow.Builder builder = Undertow.builder();
	private Undertow undertow;

	public WebServer() {
	}

	/**
	 * set server host & port
	 */
	public WebServer hostPort(String host, int port) {
		builder.addHttpListener(port, host);
		return this;
	}

	/**
	 * set routes
	 */
	public WebServer router(RoutingHandler router) {
		builder.setHandler(router);
		return this;
	}

	/**
	 * start http server
	 */
	public WebServer start() {
		undertow = builder.build();
		undertow.start();
		printStartBanner();
		return this;
	}

	/**
	 * Stop http server
	 */
	public WebServer stop() {
		if (undertow == null) {
			throw new RuntimeException("Server not started yet.");
		}
		SocketAddress address = undertow.getListenerInfo().get(0).getAddress();
		undertow.stop();
		printStopBanner(address);
		return this;
	}

	private void printStartBanner() {
		log.info("\n---------------------------------------------------------\n" +
		         "   __\n" +
		         "   \\ \\_____\n" +
		         "###[==_____>             Successfully start web server\n" +
		         "   /_/      __            {}\n" +
		         "            \\ \\_____          at http:/{}\n" +
		         "         ###[==_____>        \n" +
		         "            /_/" +
		         "\n---------------------------------------------------------\n",
		         undertow.toString(),
		         undertow.getListenerInfo().get(0).getAddress());
	}

	private void printStopBanner(SocketAddress address) {
		log.info("\n---------------------------------------------------------\n" +
		         "       , \n" +
		         "      /(  ___________    Web server \n" +
		         "     |  >:===========`   {}\n" +
		         "      )(                 at http:/{}\n" +
		         "      \"\"                 has been stopped" +
		         "\n---------------------------------------------------------\n",
		         undertow.toString(),
		         address);
	}
}
