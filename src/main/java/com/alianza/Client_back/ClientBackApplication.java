package com.alianza.Client_back;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration.class})
public class ClientBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientBackApplication.class, args);
	}

}
