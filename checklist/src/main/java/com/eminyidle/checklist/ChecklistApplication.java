package com.eminyidle.checklist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableNeo4jRepositories
@SpringBootApplication
public class ChecklistApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChecklistApplication.class, args);
    }

}
