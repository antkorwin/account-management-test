package account.management.system;

import account.management.system.config.AccountModule;
import account.management.system.controller.AccountController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.util.Methods;

import static io.undertow.Handlers.routing;


public class Application {


	public static void main(String[] args){

		Injector injector = Guice.createInjector(new AccountModule());
		AccountController controller = injector.getInstance(AccountController.class);

		Undertow server = Undertow.builder()
		                          .addHttpListener(8080, "localhost")
		                          .setHandler(routing().add(Methods.POST, "/accounts/create", new BlockingHandler(controller::create)))
		                          .build();

		server.start();
	}

}
