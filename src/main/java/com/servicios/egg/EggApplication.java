package com.servicios.egg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class EggApplication {

	public static void main(String[] args) {
		SpringApplication.run(EggApplication.class, args);
	}
}