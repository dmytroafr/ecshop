package com.echem.ecshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class EcshopApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcshopApplication.class, args);
//		ConfigurableApplicationContext context = SpringApplication.run(EcshopApplication.class, args);
//		PasswordEncoder encoder = context.getBean(PasswordEncoder.class);
//		System.out.println(encoder.encode("pass"));

	}


}
