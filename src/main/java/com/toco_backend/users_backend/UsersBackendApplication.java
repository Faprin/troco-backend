package com.toco_backend.users_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UsersBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersBackendApplication.class, args);
	}

}
