package com.eminyidle.feed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class FeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(FeedApplication.class, args);
	}

}
