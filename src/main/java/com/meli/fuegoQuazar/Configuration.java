package com.meli.fuegoQuazar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class Configuration implements ApplicationRunner {
	
	private static Logger LOG = LogManager.getLogger(Configuration.class);
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		LOG.info("Inicio de aplicacion Ok");
	}
	
}
