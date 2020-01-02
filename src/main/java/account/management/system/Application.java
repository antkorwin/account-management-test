package account.management.system;

import account.management.system.config.AccountModule;
import account.management.system.config.PropertiesReaderModule;
import account.management.system.config.TransferModule;
import account.management.system.config.WebServerModule;
import account.management.system.webserver.WebServer;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;


public class Application {

	@Getter
	private static Injector injector;


	public static void main(String[] args) {

		injector = Guice.createInjector(new PropertiesReaderModule(),
		                                new WebServerModule(),
		                                new AccountModule(),
		                                new TransferModule());

		injector.getInstance(WebServer.class).start();
	}
}
