package account.management.system;

import account.management.system.config.AccountModule;
import account.management.system.controller.AccountController;
import account.management.system.webserver.WebServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Methods;

import static io.undertow.Handlers.routing;


public class Application {


	public static void main(String[] args) {

		Injector injector = Guice.createInjector(new AccountModule());
		AccountController controller = injector.getInstance(AccountController.class);

		new WebServer().hostPort("localhost", 8080)
		               .router(routing().add(Methods.POST, "/accounts/create", new BlockingHandler(controller::create))
		                                .add(Methods.GET, "/accounts/{id}", new BlockingHandler(controller::get))
		                                .add(Methods.GET, "/accounts/list", new BlockingHandler(controller::getAll)))
		               .start();
	}

}
