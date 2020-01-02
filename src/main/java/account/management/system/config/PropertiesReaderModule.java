package account.management.system.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class PropertiesReaderModule extends AbstractModule {

	@Override
	protected void configure() {
		try {
			InputStream inputStream = PropertiesReaderModule.class.getResourceAsStream("/application.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			Names.bindProperties(binder(), properties);
		} catch (IOException ex) {
			log.error("error while reading the application.properties file", ex);
			throw new RuntimeException(ex);
		}
	}
} 