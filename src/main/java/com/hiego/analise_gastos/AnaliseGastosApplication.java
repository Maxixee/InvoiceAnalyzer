package com.hiego.analise_gastos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AnaliseGastosApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnaliseGastosApplication.class, args);
	}

}
