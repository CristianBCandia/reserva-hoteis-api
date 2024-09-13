package com.cristian.teste.reservas.hoteis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class HoteisApplication {

	public static void main(String[] args) {
		SpringApplication.run(HoteisApplication.class, args);
	}

}
