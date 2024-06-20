package com.echem.ecshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class EcshopApplication {
	public static void main(String[] args) {
		SpringApplication.run(EcshopApplication.class, args);
		System.out.println("Ready to explore");
	}
}
