package com.swe.project.contentaccess;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.swe.project.contentaccess.service.TopicService;

@SpringBootApplication(scanBasePackages = "com.swe.project")
public class ContentAccessApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentAccessApplication.class, args);
	}

	@Bean
    public CommandLineRunner dataLoader(TopicService topicService) {
        return new CommandLineRunner() {
			public void run(String... args) throws Exception {
				topicService.start();
			}
		};
	}	
}
