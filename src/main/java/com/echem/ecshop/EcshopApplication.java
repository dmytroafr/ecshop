package com.echem.ecshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

@SpringBootApplication
public class EcshopApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcshopApplication.class, args);
		System.out.println("Ready to explore");
//		Map<String,String> env = System.getenv();
//		env.forEach((s, s2) -> System.out.println("key: " + s + "| value: " + s2));
		System.out.println("HHHHHHHHHHH");
	}


}
