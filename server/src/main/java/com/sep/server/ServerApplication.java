package com.sep.server;

import com.sep.server.api.UserRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApplication {
	public static void main(String[] args) {
		UserRestController.initialize();
		SpringApplication.run(ServerApplication.class, args);
	}
}
