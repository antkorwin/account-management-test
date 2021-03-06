package account.management.system.config;

import com.google.inject.ConfigurationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PropertiesReaderModuleTest {

	@Test
	void readProperty() {
		// Arrange
		Injector injector = Guice.createInjector(new PropertiesReaderModule());
		// Act
		String port = injector.getInstance(Key.get(String.class, Names.named("server.host")));
		// Assert
		assertThat(port).isNotNull()
		                .isEqualTo("localhost");
	}

	@Test
	void tryToReadNotExistedProperty() {
		// Arrange
		Injector injector = Guice.createInjector(new PropertiesReaderModule());
		// Act
		Assertions.assertThrows(ConfigurationException.class,
		                        () -> injector.getInstance(Key.get(String.class,
		                                                           Names.named("test.not.existed.property"))));
	}
}