package account.management.system.config;


import account.management.system.controller.routing.ApplicationRouter;
import account.management.system.webserver.Router;
import account.management.system.webserver.WebServer;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

public class WebServerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Router.class).to(ApplicationRouter.class);
	}

	@Provides
	@Singleton
	public WebServer webServer(@Named("server.host") String host,
	                           @Named("server.port") String port,
	                           Router router) {

		return new WebServer().hostPort(host, Integer.parseInt(port))
		                      .router(router.getRoutes());
	}
}
