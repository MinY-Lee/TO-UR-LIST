package com.eminyidle.tour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class TourApplication {
    public static void main(String[] args) {
        SpringApplication.run(TourApplication.class, args);
    }

}
