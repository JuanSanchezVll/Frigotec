package com.frigo.frigotec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FrigotecApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrigotecApplication.class, args);
	}

}

