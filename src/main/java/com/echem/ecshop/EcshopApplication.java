package com.echem.ecshop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcshopApplication {
	private static final Logger logger = LogManager.getLogger(EcshopApplication.class);
	public static void main(String[] args) {
//		SpringApplication.run(EcshopApplication.class, args);
		ConfigurableApplicationContext context = SpringApplication.run(EcshopApplication.class, args);
		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
		System.out.println(encoder.encode("pass"));
		logger.trace("here we go");
		logger.trace("trace product controller");
		logger.debug("debug product controller");
		logger.info("info product controller");
		logger.warn("warn product controller");
		logger.error("error product controller");

	}


}
