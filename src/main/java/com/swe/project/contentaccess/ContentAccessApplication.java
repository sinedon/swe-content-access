package com.swe.project.contentaccess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.swe.project")
public class ContentAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentAccessApplication.class, args);
	}

}
