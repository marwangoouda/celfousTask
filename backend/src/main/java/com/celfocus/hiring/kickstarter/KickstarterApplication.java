package com.celfocus.hiring.kickstarter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KickstarterApplication {

	private static final Logger log = LoggerFactory.getLogger(KickstarterApplication.class);

	public static void main(String[] args) {
		log.info("Starting KickstarterApplication...");
		ConfigurableApplicationContext applicationContext =SpringApplication.run(KickstarterApplication.class, args);
		log.info("KickstarterApplication started successfully, active profiles: {}",
				String.join(",", applicationContext.getEnvironment().getActiveProfiles()));
	}

}
