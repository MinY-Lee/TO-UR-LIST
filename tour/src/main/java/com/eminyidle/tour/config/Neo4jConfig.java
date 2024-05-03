package com.eminyidle.tour.config;

import org.neo4j.cypherdsl.core.renderer.Dialect;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.neo4j.Neo4jAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = "com.eminyidle.tour.repository",transactionManagerRef = "neo4jTransactionManager")
public class Neo4jConfig extends Neo4jAutoConfiguration {

    @Value("${TOUR_NEO4J_URI}")
    private String neo4jUri;
    @Value("${TOUR_NEO4J_USER_NAME}")
    private String username;
    @Value("${TOUR_NEO4J_USER_PASSWORD}")
    private String password;

    @Bean
    public Driver getConfiguration(){
        return GraphDatabase.driver(neo4jUri, AuthTokens.basic(username,password));
    }

    @Bean(name = "neo4jTransactionManager")
    public Neo4jTransactionManager neo4jTransactionManager(){
        return new Neo4jTransactionManager(getConfiguration());
    }

    @Bean
    org.neo4j.cypherdsl.core.renderer.Configuration cypherDslConfiguration() {
        return org.neo4j.cypherdsl.core.renderer.Configuration.newConfig()
                .withDialect(Dialect.NEO4J_5).build();
    }

}
