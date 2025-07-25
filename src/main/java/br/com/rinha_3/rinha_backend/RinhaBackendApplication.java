package br.com.rinha_3.rinha_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class RinhaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RinhaBackendApplication.class, args);
	}

}
