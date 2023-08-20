package com.garage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.garage")
public class GarageAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(GarageAuthApplication.class, args);
	}
}
